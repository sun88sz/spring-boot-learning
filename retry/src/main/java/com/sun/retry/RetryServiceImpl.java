package com.sun.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author : Sun
 * @date : 2018/9/7 16:45
 */

@Slf4j
@Service("retryService")
public class RetryServiceImpl implements RetryService {

    int i = 1;

    @Retryable(value = {RuntimeException.class}, maxAttempts = 4, backoff = @Backoff(delay = 3000L, multiplier = 1))
    @Override
    public String retry() {
        i++;//生产环境此处应该为调用第三方接口，判断接口返回code
        if (i == 3) {
            return i + "";
        }
        RetryException retryException = new RetryException("连接超时");
        throw retryException;
    }


    @Recover
    public String recover(RetryException e) {
        return "6";
    }
}