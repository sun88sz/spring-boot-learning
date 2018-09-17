package com.sun;

//import org.springframework.boot.autoconfigure.data.redis.JedisConnectionConfiguration;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author : Sun
 * @date : 2018/9/17 14:06
 */
@Configuration
public class JedisConfiguration {

    
    @Bean 
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisPoolConfig poolConfig =null;
        return new JedisConnectionFactory(poolConfig);
    }
    

}
