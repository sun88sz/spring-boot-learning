package com.sun.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 同步锁注解
 *
 * @author : Sun
 * @date : 2018/9/20 14:35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SyncLock {

    /**
     * 最大等待时间
     *
     * @return
     */
    long maxWaitTime() default 0;

    /**
     * 锁过期时间
     * 默认3s
     *
     * @return
     */
    long expireTime() default 3000;

    /**
     * 时间Unit
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * redis lock的key名
     *
     * @return
     */
    String key();
}
