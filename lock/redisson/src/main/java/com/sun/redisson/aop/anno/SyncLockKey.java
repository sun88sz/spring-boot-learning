package com.sun.redisson.aop.anno;

import java.lang.annotation.*;

/**
 * 同步锁key名
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SyncLockKey {

    String value() default "";

}
