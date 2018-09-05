package com.sun;

import java.util.Map;

import com.sun.retry.MqMessageRetry;
import com.sun.retry.MqRetry;
import com.sun.retry.MqRetryCacheRedis;

import org.slf4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import static com.sun.MqDefine.*;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Configuration
public class MqConfiguration {



    /**
     * 定义一个普通的Topic Exchange
     *
     * @return
     */
    @Bean
    public TopicExchange simpleExchange() {
        return new TopicExchange(SIMPLE_EXCHANGE);
    }

    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A);
    }

    @Bean
    public Binding bindingExchangeQueueA(Queue queueA, TopicExchange simpleExchange) {
        return BindingBuilder.bind(queueA).to(simpleExchange).with(ROUTING_KEY_TOPIC_A);
    }

    /**
     * 定义一个delay的Exchange
     *
     * @return
     */
    @Bean
    public DirectExchange delayExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(DELAY_EXCHANGE).delayed().build();
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B);
    }

    @Bean
    public Binding bindingExchangeQueueB(Queue queueB, DirectExchange delayExchange) {
        return BindingBuilder.bind(queueB).to(delayExchange).with(ROUTING_KEY_TOPIC_B);
    }


    @Bean
    public MqRetry mqRetryRedis() {
        return new MqRetry() {
            @Override
            public RabbitTemplate getRabbitTemplate() {
                return null;
            }

            @Override
            public Logger getLog() {
                return null;
            }

            @Override
            public void add(String id, Object message, String exchange, String routingKey) {

            }

            @Override
            public void set(String id, MqMessageRetry mqRetry) {

            }

            @Override
            public Map<String, MqMessageRetry> getAll() {
                return null;
            }

            @Override
            public void del(String id) {

            }
        };
    }
//
//    @Bean
//    @ConditionalOnMissingBean(RedisTemplate.class)
//    public MqRetry mqRetryMap() {
//        return new MqRetry() {
//            @Override
//            public RabbitTemplate getRabbitTemplate() {
//                return null;
//            }
//
//            @Override
//            public Logger getLog() {
//                return null;
//            }
//
//            @Override
//            public void add(String id, Object message, String exchange, String routingKey) {
//
//            }
//
//            @Override
//            public void set(String id, MqMessageRetry mqRetry) {
//
//            }
//
//            @Override
//            public Map<String, MqMessageRetry> getAll() {
//                return null;
//            }
//
//            @Override
//            public void del(String id) {
//
//            }
//        };
//    }

}
