package com.sun;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Sun
 * @date : 2018/9/5 15:10
 */
@Configuration
public class CommonConfiguration {

    /**
     * 雪花算法 分布式id
     * @return
     */
    @Bean
    public SnowflakeIdWorker snowflakeIdWorker(){
        return new SnowflakeIdWorker(0,1);
    }
}
