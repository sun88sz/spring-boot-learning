package com.sun;

import com.sun.lock.RedisLockManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description: <br/>
 * Date: 2018-09-15
 *
 * @author Sun
 */
@Configuration
@ConditionalOnClass(RedisAutoConfiguration.class)
@Import(RedisAutoConfiguration.class)
public class RedisLockConfiguration {

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisLockManager redisLock(RedisTemplate redisTemplate) {
        return new RedisLockManager(redisTemplate);
    }
}
