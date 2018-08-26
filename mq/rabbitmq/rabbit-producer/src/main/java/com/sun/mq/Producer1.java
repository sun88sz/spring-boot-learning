package com.sun.mq;

import org.springframework.amqp.core.AmqpTemplate;
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

    public User test() {

        User user = new User();
        Random random = new Random();
        int i = random.nextInt();
        long l = random.nextLong();
        user.setAge(i);
        user.setId(l);
        user.setName("name");

        amqpTemplate.convertAndSend("supplierExchange", "com.sun.topic1", user);

        return user;
    }
}
