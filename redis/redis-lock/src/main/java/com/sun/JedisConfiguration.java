package com.sun;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @author : Sun
 * @date : 2018/9/17 14:06
 */
@Configuration
public class JedisConfiguration {
    
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}
