package com.sun.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

/**
 * @author : Sun
 * @date : 2018/9/7 16:47
 */
public interface RetryService {
    @Retryable(value = {RuntimeException.class},maxAttempts = 4,backoff = @Backoff(delay = 1000L,multiplier = 1))
    String retry();
}
