package com.sun.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Service
public class Producer1 {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private User createUser(){
        User user = new User();
        Random random = new Random();
        int i = random.nextInt();
        long l = random.nextLong();
        user.setAge(i);
        user.setId(l);
        user.setName("name");

        return user;
    }


    public User test() {
        User user = createUser();
        amqpTemplate.convertAndSend("simpleExchange", "com.sun.topicA", user);
        return user;
    }


    public User test2() {

        User user = createUser();

        // 延时消息
        int times = 5000;
        MessagePostProcessor processor = message -> {
            // 设置消息持久化
//            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置延迟时间
            message.getMessageProperties().setDelay(times);
            return message;
        };
        amqpTemplate.convertAndSend("delayExchange", "com.sun.topicB", user, processor);
        // 如果不加 processor，队列将不会延时
//        amqpTemplate.convertAndSend("delayExchange", "com.sun.topicB", user);
        return user;
    }
}
