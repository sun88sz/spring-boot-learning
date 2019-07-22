package com.sun.mq;

import com.alibaba.fastjson.JSON;
import com.sun.SnowflakeIdWorker;
import com.sun.bean.User;
import com.sun.retry.MqRetry;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sun.MqDefine.ROUTING_KEY_TOPIC_A;
import static com.sun.MqDefine.SIMPLE_EXCHANGE;


/**
 * 加上
 * Date: 2018-08-26
 *
 * @author Sun
 */
@Service
public class Producer2 {

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private MqRetry mqRetry;

    /**
     * 第一种方式：
     * 把 发送的信息放在CorrelationData里，传输
     * 在回调setConfirmCallback 的方法中获取 data 数据保存
     * 由其他监控线程重新发送
     * <p>
     * 问题：当未收到ack回应时 会丢数据
     *
     * @return
     */
    public User test() {
        User user = User.createUser();

        String json = JSON.toJSONString(user);
        // CorrelationData
        CorrelationData correlationData = new RetryCorrelationData(String.valueOf(snowflakeIdWorker.nextId()), json);

        // simpleExchange2 不存在 会发送失败
        amqpTemplate.convertAndSend("simpleExchange2", "com.sun.topicA", user, correlationData);

        return user;
    }

    /**
     * 第二种方式：
     * 先保存
     * 收到回调时，删除缓存数据
     * <p>
     * 问题：必须保证consumer的幂等性，有可能发送重复数据
     *
     * @return
     */
    public User test2() {
        User user = User.createUser();

        // 此处其实也有先后问题
        // 先保存 后发送
        // 重发
        String messageId = String.valueOf(snowflakeIdWorker.nextId());
        mqRetry.add(messageId, user, SIMPLE_EXCHANGE, ROUTING_KEY_TOPIC_A);

        // simpleExchange2 不存在 会发送失败
        CorrelationData correlationData = new CorrelationData(messageId);
        amqpTemplate.convertAndSend("simpleExchange2", ROUTING_KEY_TOPIC_A, user, correlationData);

        return user;
    }


}
