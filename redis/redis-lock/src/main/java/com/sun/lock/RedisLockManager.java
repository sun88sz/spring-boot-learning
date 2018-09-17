package com.sun.lock;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */
public class RedisLockManager {

    /**
     * redis连接
     */
    private RedisTemplate redisTemplate;
    private String rootKey;

    public RedisLockManager(RedisTemplate redisTemplate, String rootKey) {
        this.redisTemplate = redisTemplate;
        this.rootKey = rootKey;
    }

    /**
     * 获取锁
     */
    public RedisLock getRedisLock(String lockKey) {
        return new RedisLock(redisTemplate, rootKey, lockKey);
    }
}
