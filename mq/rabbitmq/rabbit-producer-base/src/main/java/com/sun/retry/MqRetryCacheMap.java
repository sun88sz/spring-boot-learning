package com.sun.retry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mq重试
 *
 * @author Sun
 */
public class MqRetryCacheMap implements MqRetry {

    private Logger log = LoggerFactory.getLogger(MqRetryCacheMap.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, MqMessageRetry> map = new ConcurrentHashMap<>();


    @Override
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    @Override
    public Logger getLog() {
        return log;
    }

    /**
     * 添加
     *
     * @param id
     * @param message
     */
    @Override
    public void add(String id, Object message, String exchange, String routingKey) {
        map.put(id, new MqMessageRetry(System.currentTimeMillis(), message, exchange, routingKey));
    }

    /**
     * 重置
     *
     * @param id
     */
    @Override
    public void set(String id, MqMessageRetry mqRetry) {
        map.put(id, mqRetry);
    }

    @Override
    public Map<String, MqMessageRetry> getAll() {
        return map;
    }


    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) {
        map.remove(id);
    }

}