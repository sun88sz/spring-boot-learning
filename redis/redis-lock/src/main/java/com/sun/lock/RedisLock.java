package com.sun.lock;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */
public class RedisLock {

    private String key;
    private boolean lock = false;
    private int times = 5;

    private final RedisTemplate redisClient;
    private final RedisConnection redisConnection;
    private final Jedis jedis;
    private String uuid;

    /**
     * @param redisTemplate
     * @param rootKey
     * @param lockKey
     */
    public RedisLock(RedisTemplate redisTemplate, String rootKey, String lockKey) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("RedisTemplate 不能为null!");
        }
        this.key = rootKey + ":" + lockKey;
        this.redisClient = redisTemplate;
        this.redisConnection = redisTemplate.getConnectionFactory().getConnection();
        this.uuid = UUID.randomUUID().toString();

        // 根据 redisTemplate 获取 jedis
        this.jedis = ((JedisConnection) redisConnection).getJedis();
    }


    /**
     * @param maxWaitTime timeout的时间范围内轮询锁
     * @param expireTime  设置锁超时时间
     * @return true, 获取锁成功; false, 获取锁失败.
     */
    public boolean lock(long maxWaitTime, long expireTime, final TimeUnit unit) {
        // 以毫秒为单位
        long beginTime = System.currentTimeMillis();
        maxWaitTime = unit.toMillis(maxWaitTime);
        try {
            // 在timeout的时间范围内不断轮询锁
            do {
                // 锁不存在的话，设置锁并设置锁过期时间，即加锁
                if (atomicLock(expireTime, unit)) {
                    return true;
                }

                // 线程睡眠
                // 按次数最多获取5次锁
                Thread.sleep(maxWaitTime / times);
            }
            while (System.currentTimeMillis() - beginTime < maxWaitTime);
        } catch (Exception e) {
            throw new RuntimeException("locking error");
        }
        return false;
    }

    /**
     * 无等待时间的lock
     *
     * @param keyExpireTime
     * @param unit
     * @return
     */
    public boolean lock(long keyExpireTime, final TimeUnit unit) {
        return lock(0, keyExpireTime, unit);
    }

    /**
     * 原子加锁
     *
     * @return
     */
    private boolean atomicLock(long expireTime, final TimeUnit unit) {
        // 转换格式
        expireTime = unit.toMillis(expireTime);
        // NX setnx
        // PX ms
        String result = jedis.set(this.key, this.uuid, "NX", "PX", expireTime);
        if ("OK".equals(result)) {
            this.lock = true;
            return true;
        }
        return false;
    }


    /**
     * 一般加锁
     *
     * @return
     */
    private boolean commonLock(long expireTime, final TimeUnit unit) {
        if (this.redisClient.opsForValue().setIfAbsent(this.key, this.uuid)) {
            // 设置锁失效时间, 防止永久阻塞
            this.redisClient.expire(key, expireTime, unit);
            this.lock = true;
            return true;
        }
        return false;
    }


    /**
     * 解锁
     */
    public void unlock() {
        if (this.lock) {
            atomicUnlock();
            lock = false;
        }
    }

    /**
     * 释放锁
     * 防止释放别人的锁
     */
    private void commonUnlock() {
        Object o = redisClient.opsForValue().get(key);
        if (o != null && (o).equals(this.uuid)) {
            redisClient.delete(key);
        }
    }

    /**
     * 释放锁
     * 使用eval表达式 执行原子操作
     */
    private void atomicUnlock() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        // 正确返回1 错误返回0
        Object result = jedis.eval(script, Collections.singletonList(this.key), Collections.singletonList(this.uuid));
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
