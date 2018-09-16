package com.sun.lock;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock {
    private String key;
    private boolean lock = false;
    private int TIMES = 5;

    private final RedisTemplate redisClient;
    private final RedisConnection redisConnection;
    private String uuid;

    /**
     * @param lockKey     key名
     * @param redisClient
     */
    public RedisLock(String lockKey, RedisTemplate redisClient) {
        if (redisClient == null) {
            throw new IllegalArgumentException("RedisTemplate 不能为null!");
        }
        this.key = "REDIS_LOCK:" + lockKey;
        this.redisClient = redisClient;
        this.redisConnection = redisClient.getConnectionFactory().getConnection();
        this.uuid = UUID.randomUUID().toString();
    }


    /**
     * @param maxWaitTime   timeout的时间范围内轮询锁
     * @param keyExpireTime 设置锁超时时间
     * @return true, 获取锁成功; false, 获取锁失败.
     */
    public boolean lock(long maxWaitTime, long keyExpireTime, final TimeUnit unit) {
        // 以毫秒为单位
        long beginTime = System.currentTimeMillis();
        maxWaitTime = unit.toMillis(maxWaitTime);
        try {
            // 在timeout的时间范围内不断轮询锁
            do {
                // 锁不存在的话，设置锁并设置锁过期时间，即加锁
                if (this.redisClient.opsForValue().setIfAbsent(this.key, this.uuid)) {
                    // 设置锁失效时间, 防止永久阻塞
                    this.redisClient.expire(key, keyExpireTime, unit);
                    this.lock = true;
                    return true;
                }

                // 线程睡眠
                // 最多获取4次锁
                Thread.sleep(maxWaitTime / TIMES);
            }
            while (System.currentTimeMillis() - beginTime < maxWaitTime);
        } catch (Exception e) {
            throw new RuntimeException("locking error");
        }
        return false;
    }


    /**
     * 释放锁
     * 防止释放别人的锁
     */
    public void unlock() {
        if (this.lock) {
            Object o = redisClient.opsForValue().get(key);
            if (o != null && (o).equals(this.uuid)) {
                redisClient.delete(key);
            }
            lock = true;
        }
    }

    public boolean lock(long keyExpireTime, final TimeUnit unit) {
        return lock(0, keyExpireTime, unit);
    }


    /**
     * @return current redis-server time in milliseconds.
     */
    public long getRedisTime() {
        return this.redisConnection.time();
    }

    private void closeConnection() {
        if (!this.redisConnection.isClosed()) {
            this.redisConnection.close();
        }
    }
}
