package com.sun.lock.anno;

import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 同步锁注解拦截器
 *
 * @author Sun
 * @date 2018-09-20
 */
@Aspect
@Component
public class SyncLockAspect {

    final static Logger log = LoggerFactory.getLogger(SyncLockAspect.class);

    @Autowired
    private RedisLockManager redisLockManager;

    @Pointcut("@annotation(com.sun.lock.anno.SyncLock)")
    public void syncLockAspect() {
    }

    @Around(value = "syncLockAspect()")
    public void around(ProceedingJoinPoint point) throws Throwable {

        Class clazz = point.getTarget().getClass();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        SyncLock anno = method.getAnnotation(SyncLock.class);

        String keyName = anno.key();
        if (StringUtils.isBlank(keyName)) {
            keyName = clazz.getSimpleName() + ":" + method.getName();
        }

        long expireTime = anno.expireTime();
        long maxWaitTime = anno.maxWaitTime();
        TimeUnit timeUnit = anno.timeUnit();

        RedisLock redisLock = redisLockManager.getRedisLock(keyName);
        try {
            if (redisLock.lock(maxWaitTime, expireTime, timeUnit)) {
                point.proceed(point.getArgs());
            }
        } finally {
            redisLock.unlock();
        }
    }
}
