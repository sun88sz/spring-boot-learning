package com.sun.mq;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.sun.bean.User;

/**
 * 普通发送
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Service
public class Producer1 {

    @Autowired
    private RabbitTemplate amqpTemplate;


    public User test() {
        User user = User.createUser();
        amqpTemplate.convertAndSend("simpleExchange", "com.sun.topicA", user);
        return user;
    }


    /**
     * 延时消息
     */
    int times = 5000;

    /**
     * 设置延迟时间
     */
    MessagePostProcessor processor = message -> {
        // 设置消息持久化
        //            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        message.getMessageProperties().setDelay(times);
        return message;
    };

    /**
     * 延时队列
     *
     * @return
     */
    public User test2() {
        User user = User.createUser();

        amqpTemplate.convertAndSend("delayExchange", "com.sun.topicB", user, processor);
        // 如果不加 processor，队列将不会延时
//        amqpTemplate.convertAndSend("delayExchange", "com.sun.topicB", user);
        return user;
    }
}
