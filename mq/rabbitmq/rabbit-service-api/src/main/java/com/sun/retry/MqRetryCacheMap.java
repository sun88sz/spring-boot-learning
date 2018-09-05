package com.sun.retry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mq重试
 *
 * @author Sun
 */
public class MqRetryCacheMap implements MqRetry {
    private boolean stop = false;

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
        map.put(id, new MqMessageRetry(System.currentTimeMillis(), 0, message, exchange, routingKey));
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

        return null;
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


    @Override
    public void createThread() {
        new Thread(() -> {
            while (!stop) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();
                for (String key : map.keySet()) {
                    MqMessageRetry mr = map.get(key);
                    if (mr != null) {
                        long time = mr.getTime();
                        // 等待消息发送10s后 进行重试操作
                        if (time - now > 10000) {
                            // 重新发送 等待回调
                            CorrelationData data = new CorrelationData(key);
                            // 覆盖
                            mr.setTimes(mr.getTimes() + 1);
                            map.put(key, mr);
                            rabbitTemplate.convertAndSend(mr.getExchange(), mr.getRoutingKey(), mr.getMessage(), data);
                        }
                    }
                }
            }
        }).start();
    }
}