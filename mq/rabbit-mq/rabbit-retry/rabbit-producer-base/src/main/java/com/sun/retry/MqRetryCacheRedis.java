package com.sun.retry;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : Sun
 * @date : 2018/9/5 19:03
 */
public class MqRetryCacheRedis implements MqRetry {

    @Getter
    private Logger log = LoggerFactory.getLogger(MqRetryCacheRedis.class);

    @Getter
    private RabbitTemplate rabbitTemplate;

    private RedisTemplate redisTemplate;

    private String MQ_RETRY;


    public MqRetryCacheRedis(RedisTemplate redisTemplate, RabbitTemplate rabbitTemplate, String MQ_RETRY) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.MQ_RETRY = MQ_RETRY;
    }

    /**
     * 初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        defineConfirmCallback();

        // 初始化Map
        Map entries = redisTemplate.opsForHash().entries(MQ_RETRY);
        if (entries == null) {
            redisTemplate.opsForHash().putAll(MQ_RETRY, new HashMap<String, MqMessageRetry>());
        }

        // 启动监控线程
        createThread();
    }

    /**
     * 添加
     *
     * @param id
     * @param message
     */
    @Override
    public void add(String id, Object message, String exchange, String routingKey) {
        redisTemplate.opsForHash().put(MQ_RETRY, id, new MqMessageRetry(System.currentTimeMillis(), message, exchange, routingKey));
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
        redisTemplate.opsForHash().delete(MQ_RETRY, id);
    }

}
