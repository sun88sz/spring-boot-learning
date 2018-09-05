package com.sun.retry;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : Sun
 * @date : 2018/9/5 17:03
 */
public class MqRetryCacheRedis implements MqRetry {
    private Logger log = LoggerFactory.getLogger(MqRetryCacheRedis.class);

    @Override
    public Logger getLog() {
        return log;
    }
    
    @Getter
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${mq_message_retry:MQ_MESSAGE_RETRY}")
    private static String MQ_RETRY;

    /**
     * 初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        defineConfirmCallback();
        createThread();
        Map entries = redisTemplate.opsForHash().entries(MQ_RETRY);
        if (entries == null) {
            redisTemplate.opsForHash().putAll(MQ_RETRY, new HashMap<String, MqMessageRetry>());
        }
    }

    /**
     * 添加
     *
     * @param id
     * @param message
     */
    @Override
    public void add(String id, Object message, String exchange, String routingKey) {
        MqMessageRetry msg = new MqMessageRetry();
        msg.setMessage(message);
        msg.setTimes(1);
        msg.setTime(System.currentTimeMillis());
        msg.setExchange(exchange);
        msg.setRoutingKey(routingKey);

        redisTemplate.opsForHash().put(MQ_RETRY, id, message);
    }

    /**
     * 重置
     *
     * @param id
     */
    @Override
    public void set(String id, MqMessageRetry msg) {
        redisTemplate.opsForHash().put(MQ_RETRY, id, msg);
    }

    /**
     * 获取所有
     */
    @Override
    public Map<String, MqMessageRetry> getAll() {
        Map<String, MqMessageRetry> entries = redisTemplate.opsForHash().entries(MQ_RETRY);
        return entries;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) {
        redisTemplate.opsForHash().delete(MQ_RETRY , id);
    }
    
}
