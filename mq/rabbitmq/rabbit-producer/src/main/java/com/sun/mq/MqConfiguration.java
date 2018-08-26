package com.sun.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Queue queueA() {
      return new Queue("queue.A", true);
    }

    @Bean
    public TopicExchange supplierExchange() {
      return new TopicExchange("supplierExchange");
    }

    @Bean
    public Binding bindingExchangePurchaserFriendApplySupplier(Queue queueA, TopicExchange supplierExchange) {
        return BindingBuilder.bind(queueA).to(supplierExchange).with("com.sun.topic1");
    }
}
