package com.sun;

import com.sun.retry.MqRetry;
import com.sun.retry.MqRetryCacheMap;
import com.sun.retry.MqRetryCacheRedis;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description: <br/>
 * Date: 2018-09-06
 *
 * @author Sun
 */
@Configuration
public class MqRetryConfiguration {

    @Configuration
    @ConditionalOnClass({RedisAutoConfiguration.class, RedisTemplate.class})
    @Import({RedisAutoConfiguration.class})
    protected class RedisConfiguration {

        @Value("${mq_message_retry:MQ_MESSAGE_RETRY}")
        private String MQ_RETRY;

        @Bean
        @ConditionalOnBean(RedisTemplate.class)
        public MqRetry mqRetryRedis(RedisTemplate redisTemplate, RabbitTemplate rabbitTemplate) {
            return new MqRetryCacheRedis(redisTemplate, rabbitTemplate, MQ_RETRY);
        }
    }


    @Bean
    @ConditionalOnMissingBean(MqRetry.class)
    public MqRetry mqRetryMap() {
        return new MqRetryCacheMap();
    }

}
