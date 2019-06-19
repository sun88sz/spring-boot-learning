package com.sun.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Sun
 * @date : 2018/9/14 16:05
 */
@Configuration
@EnableConfigurationProperties
public class RedissonConfiguration {

    @Autowired
    private RedissonProperties redssionProperties;

    /**
     * 单机模式自动装配
     *
     * @return
     */
    @Bean
    public RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + "127.0.0.1:6379");
//                .setTimeout(redssionProperties.getTimeout())
//                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
//                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

//        if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
//            serverConfig.setPassword(redssionProperties.getPassword());
//        }

        return Redisson.create(config);
    }

}
