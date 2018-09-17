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
public @interface SynJob {

    long waitTime() default 0;

    long expireTime() default 60;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    String jobName() default "";
}
