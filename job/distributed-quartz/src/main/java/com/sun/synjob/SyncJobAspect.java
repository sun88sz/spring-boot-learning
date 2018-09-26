package com.sun.synjob;

import com.sun.istack.internal.NotNull;
import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */
@Aspect
@Component
public class SyncJobAspect implements EmbeddedValueResolverAware {

    final static Logger log = LoggerFactory.getLogger(SyncJobAspect.class);

    @NotNull
    private StringValueResolver resolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * job同步锁key前缀
     */
    private static String SynJobKey = "REDIS_SYN_JOB";

    @Autowired
    private RedisLockManager redisLockManager;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义切入点
     * 切自定义注解SynJob
     */
    @Pointcut("@annotation(com.sun.synjob.SyncJob)")
    public void synJobAspect() {
    }

    @Around(value = "synJobAspect()")
    public void around(ProceedingJoinPoint point) throws Throwable {

        Class clazz = point.getTarget().getClass();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        SyncJob annotation = method.getAnnotation(SyncJob.class);
        Scheduled annoScheduled = method.getAnnotation(Scheduled.class);
        if (annoScheduled == null) {
            point.proceed(point.getArgs());
        }

        // cron 表达式
        String cron = resolver.resolveStringValue(annoScheduled.cron());
        
        // 定义过期时间
        long timeBetween = getTimeBetween(cron) * 4 / 5;


        String keyName = annotation.jobName();
        if (StringUtils.isBlank(keyName)) {
            keyName = clazz.getName() + ":" + method.getName();
        }

        RedisLock redisLock = redisLockManager.getRedisLock(keyName);
        try {
            String key = SynJobKey + ":" + keyName;
            if (redisLock.lock(annotation.waitTime(), annotation.expireTime(), annotation.timeUnit())) {
                Object lastTime = redisTemplate.opsForValue().get(key);

                // 本次执行时间是否被其他job执行过
                if (lastTime == null) {
                    // 设置最新更新时间
                    redisTemplate.opsForValue().set(key, Long.valueOf(System.currentTimeMillis()).toString());
                    redisTemplate.expire(key, timeBetween, TimeUnit.MILLISECONDS);
                    // 执行逻辑
                    point.proceed(point.getArgs());
                }
            }
        } finally {
            redisLock.unlock();
        }
    }


    private static Long getTimeBetween(String cron) {
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 获取下一次执行时间
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 2);

            if (CollectionUtils.isNotEmpty(dates)) {
                long between = dates.get(1).getTime() - dates.get(0).getTime();
                return between;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }



    public static Date getNextTriggerTime(String cron) {
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 这个是重点，一行代码搞定
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);

            if (CollectionUtils.isNotEmpty(dates)) {
                return dates.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
