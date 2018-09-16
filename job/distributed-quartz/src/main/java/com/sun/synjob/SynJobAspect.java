package com.sun.synjob;

import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisLockManager redisLockManager;
    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.sun.synjob.SynJob)")
    public void synJobAspect() {
    }

    @Around(value = "synJobAspect()")
    public void around(ProceedingJoinPoint point) throws Throwable {

        Method target = (Method) point.getTarget();
        SynJob annotation = target.getAnnotation(SynJob.class);
        String cron = annotation.cron();
        Date nextTriggerTime = getNextTriggerTime(cron);

        RedisLock redisLock = redisLockManager.getRedisLock("");
        if (redisLock.lock(annotation.waitTime(), annotation.expireTime(), annotation.timeUnit())) {
            long lastTime = (long) redisTemplate.opsForValue().get("XX:");
            if (nextTriggerTime.getTime() > lastTime) {
                point.proceed(point.getArgs());
                redisTemplate.opsForValue().set("XX:", nextTriggerTime.getTime());
            }
        }
        redisLock.unlock();
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
