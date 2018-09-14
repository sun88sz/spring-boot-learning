package com.sun.quartz;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author : Sun
 * @date : 2018/9/14 11:42
 */
@Configuration
@EnableScheduling
@EnableAsync
public class SchedulingConfiguration {

    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.initialize();
        return executor;
    }

    @Value("${scheduled.cron.A}")
    private String cron_A;

    @Bean
    public ScheduledDistributed scheduledDistributed() {
        return new ScheduledDistributed(cron_A);
    }
}
