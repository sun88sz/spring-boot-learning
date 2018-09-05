package com.sun.retry;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author : Sun
 * @date : 2018/9/5 17:03
 */
public interface MqRetry extends InitializingBean {
    RabbitTemplate getRabbitTemplate();

    Logger getLog();

    /**
     * @param id
     * @param message
     * @param exchange
     * @param routingKey
     */
    void add(String id, Object message, String exchange, String routingKey);

    /**
     * @param id
     * @param mqRetry
     */
    void set(String id, MqMessageRetry mqRetry);

    /**
     * @return
     */
    Map<String, MqMessageRetry> getAll();

    /**
     * @param id
     */
    void del(String id);

    /**
     *
     */
    default void createThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();
                Map<String, MqMessageRetry> map = getAll();

                if (map.entrySet() != null) {

                    map.entrySet().stream().forEach(
                            e -> {
                                String key = e.getKey();
                                MqMessageRetry mr = e.getValue();
                                if (mr != null) {
                                    long time = mr.getTime();
                                    // 等待消息发送10s后 进行重试操作
                                    if (time - now > 10000) {
                                        // 重新发送 等待回调
                                        CorrelationData data = new CorrelationData(key);
                                        // 覆盖
                                        getRabbitTemplate().convertAndSend(mr.getExchange(), mr.getRoutingKey(), mr.getMessage(), data);
                                    }
                                }
                            }
                    );
                }
            }
        }).start();
    }


    /**
     * 定义 如何处理 mq发送回调
     * 发送错误需进行重试
     *
     * @throws Exception
     */
    default void defineConfirmCallback() {
        getRabbitTemplate().setConfirmCallback((correlationData, ack, cause) -> {
            // 如果失败
            if (!ack) {
                getLog().error("消息发送失败：" + cause);
                if (correlationData != null) {
                    getLog().error("数据：", correlationData.toString());
                }
            }
            // 成功
            else {
                this.del(correlationData.getId());
            }
        });
    }


    /**
     * 初始化
     *
     * @throws Exception
     */
    @Override
    default void afterPropertiesSet() throws Exception {
        defineConfirmCallback();
        createThread();
    }
}
