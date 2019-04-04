package com.sun.retry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Sun
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MqMessageRetry implements Serializable {
    /**
     * 生成时间
     */
    private long time;

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
