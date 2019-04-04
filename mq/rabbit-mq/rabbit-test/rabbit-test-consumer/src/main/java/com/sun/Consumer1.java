package com.sun;

import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description: <br/>
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Component
public class Consumer1 {


    @RabbitListener(queuesToDeclare = @Queue("queue.A") )
    public void aa(Channel channel, Message message, User user) throws IOException {
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd hh:mm:ss:ms"));
        throw new RuntimeException("xxxxxxxxxxx");
    }
}
