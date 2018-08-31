package com.sun.mq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Configuration
public class MqConfiguration {


    /**
     * 定义一个普通的Exchange
     *
     * @return
     */
    @Bean
    public TopicExchange simpleExchange() {
        return new TopicExchange("simpleExchange");
    }
    
    @Bean
    public Queue queueA() {
        return new Queue("queue.A");
    }

    @Bean
    public Binding bindingExchangeQueueA(Queue queueA, TopicExchange simpleExchange) {
        return BindingBuilder.bind(queueA).to(simpleExchange).with("com.sun.topicA");
    }


    /**
     * 定义一个delay的Exchange
     *
     * @return
     */
    @Bean
    public DirectExchange delayExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange("delayExchange").delayed().build();
    }


    @Bean
    public Queue queueB() {
        return new Queue("queue.B");
    }

    @Bean
    public Binding bindingExchangeQueueB(Queue queueB, DirectExchange delayExchange) {
        return BindingBuilder.bind(queueB).to(delayExchange).with("com.sun.topicB");
    }
}
