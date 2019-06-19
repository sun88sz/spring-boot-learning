package com.sun.redisson.aop.anno;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 同步锁注解
 *
 * @author : Sun
 * @date : 2018/9/20 14:35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SyncLock {

    /**
     * 获取锁最大等待时间
     * 默认2s
     *
     * @return
     */
    long maxWaitTime() default 2000;

    /**
     * 锁过期时间
     * 默认5s
     *
     * @return
     */
    long expireTime() default 5000;

    /**
     * 时间Unit单位
     * 默认毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 是否有事务
     */
    boolean isTransactional() default false;

    /**
     * redis lock的key名
     * 如果有多个表示需要获取多个同步锁
     *
     * @return
     */
    SyncLockKey[] keys();
}
