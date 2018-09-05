package com.sun.mq;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author : Sun
 * @date : 2018/9/5 14:52
 */
@Data
public class RetryCorrelationData extends CorrelationData {

    /**
     * 数据
     */
    private String data;

    /**
     * Construct an instance with a null Id.
     *
     * @since 1.6.7
     */
    public RetryCorrelationData() {
    }

    /**
     * Construct an instance with the supplied id.
     *
     * @param id the id.
     */
    public RetryCorrelationData(String id, String data) {
        super(id);
        this.data = data;
    }
}
