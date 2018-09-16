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

    public RedisLockManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取锁
     */
    public RedisLock getRedisLock(String lockKey) {
        return new RedisLock(lockKey, redisTemplate);
    }
}
