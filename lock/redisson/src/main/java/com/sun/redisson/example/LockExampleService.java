package com.sun.redisson.example;

import com.sun.redisson.aop.anno.SyncLock;
import com.sun.redisson.aop.anno.SyncLockKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LockExampleService {

    public static final String KEY1 = "KEY1";

    /**
     * @param parameter
     * @return
     */
    @SyncLock(timeUnit = TimeUnit.SECONDS, keys = {
            @SyncLockKey("'" + KEY1 + "_'+#parameter.id"),
            @SyncLockKey("'Key2_'+#parameter.name")
    })
    public void doing(LockParameter parameter) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
    }

}
