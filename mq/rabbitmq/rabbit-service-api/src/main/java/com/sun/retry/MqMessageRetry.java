package com.sun.retry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sun
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MqMessageRetry {
    /**
     * 当前时间
     */
    private long time;
    /**
     * 重试次数
     */
    private int times;
    /**
     * 内容
     */
    private Object message;

    /**
     *
     */
    private String exchange;
    /**
     *
     */
    private String routingKey;
}
