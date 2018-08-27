package com.sun.mq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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


    @Bean
    public TopicExchange simpleExchange() {
      return new TopicExchange("simpleExchange");
    }


    /**
     * 使用 exchange的方式
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue("queue.A", true);
    }

    @Bean
    public Binding bindingExchangeQueueA(Queue queueA, TopicExchange simpleExchange) {
        return BindingBuilder.bind(queueA).to(simpleExchange).with("com.sun.topicA");
    }


    /**
     * 定义一个delay的exchange
     * @return
     */
    @Bean
    public TopicExchange delayExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange("simpleExchange").delayed().withArgument("x-delayed-message", true).build();
    }


    @Bean
    public Queue queueB() {
        return new Queue("queue.B", true);
    }

    @Bean
    public Binding bindingExchangeQueueB(Queue queueB, TopicExchange delayExchange) {
        return BindingBuilder.bind(queueB).to(delayExchange).with("com.sun.topicB");
    }
}
