package com.sun.synjob;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SyncJob {

    /**
     * 最大等待被占用的锁的时间
     * 默认0，不等待，即如果被占用立即返回失败
     * 以TimeUnit为单位
     * @return
     */
    long waitTime() default 0;

    /**
     * 锁过期时间
     * 默认10s
     * 以TimeUnit为单位
     * @return
     */
    long expireTime() default 10000;

    /**
     * 时间单位
     * 默认毫秒 ms
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * job名称
     * 唯一表示
     * 如果不填，以类名+方法名作为标识
     * @return
     */
    String jobName() default "";
}
