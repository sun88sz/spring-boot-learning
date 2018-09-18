package com.sun;

import com.sun.lock.RedisLockManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

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

    @Value("${redis-lock:REDIS_LOCK}")
    private String REDIS_LOCK_ROOT_KEY;

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public RedisLockManager redisLock(StringRedisTemplate redisTemplate) {
        return new RedisLockManager(redisTemplate, REDIS_LOCK_ROOT_KEY);
    }
}
