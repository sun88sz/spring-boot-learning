package com.sun.synjob;

import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */
@Aspect
@Component
public class SynJobAspect {

    final static Logger log = LoggerFactory.getLogger(SynJobAspect.class);

    private static String SynJobKey = "REDIS_SYN_JOB";

    @Autowired
    private RedisLockManager redisLockManager;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.sun.synjob.SynJob)")
    public void synJobAspect() {
    }

    @Around(value = "synJobAspect()")
    public void around(ProceedingJoinPoint point) throws Throwable {

        Class clazz = point.getTarget().getClass();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        SynJob annotation = method.getAnnotation(SynJob.class);
        Scheduled annoScheduled = method.getAnnotation(Scheduled.class);
        if (annoScheduled == null) {
            point.proceed(point.getArgs());
        }

        String cron = annoScheduled.cron();
        Date nextTriggerTime = getNextTriggerTime(cron);

        String keyName = annotation.jobName();
        if (StringUtils.isBlank(keyName)) {
            keyName = clazz.getName() + ":" + method.getName();
        }

        RedisLock redisLock = redisLockManager.getRedisLock(keyName);
        try {
            if (redisLock.lock(annotation.waitTime(), annotation.expireTime(), annotation.timeUnit())) {
                Object lastTime = redisTemplate.opsForValue().get(SynJobKey + ":" + keyName);
                // 本次执行时间是否被其他job执行过
                if (lastTime == null || nextTriggerTime.getTime() > Long.valueOf((String) lastTime)) {
                    // 设置最新更新时间
                    redisTemplate.opsForValue().set(SynJobKey + ":" + keyName, String.valueOf(nextTriggerTime.getTime()));
                    // 执行逻辑
                    point.proceed(point.getArgs());
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisLock.unlock();
        }
    }


    public static Date getNextTriggerTime(String cron) {
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 获取下一次执行时间
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
