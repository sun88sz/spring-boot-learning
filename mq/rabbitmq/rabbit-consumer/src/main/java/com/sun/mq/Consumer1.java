package com.sun.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RabbitListener(queues = "queue.A")
    public void aa(Channel channel, Message message, User user) throws IOException {
        System.out.println(user);

        System.out.println(user.getId());
        System.out.println(user.getName());
        throw new RuntimeException("xxxxxxxxxxx");
    }


    public void xx(Channel channel, Message message, User user) throws IOException {

        //手动应答
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        // deliveryTag:该消息的index
        //multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
        //requeue：被拒绝的是否重新入队列
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);


        //ack返回false，并重新回到队列，api里面解释得很清楚
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//拒绝消息
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);



        //重新发送消息到队尾
//        channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
//                message.getMessageProperties().getReceivedRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN,
//                JSON.toJSONBytes(new Object()));

        System.out.println(user);

    }
}
