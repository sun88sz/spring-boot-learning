package com.sun.lock;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;
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


    private final RedisTemplate redisTemplate;
    private final RedisConnection redisConnection;
    private String uuid;

    /**
     * 等待时间内，线程最多唤醒次数
     */
    private int times = 20;
    /**
     * 等待时间内，线程最小唤醒间隔（ms）
     */
    private long minSleepTime = 100L;

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
        this.redisTemplate = redisTemplate;
        this.redisConnection = redisTemplate.getConnectionFactory().getConnection();
        this.uuid = UUID.randomUUID().toString();
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

        long sleepTime = calSleepTime(maxWaitTime);
        try {
            // 在timeout的时间范围内不断轮询锁
            do {
                // 锁不存在的话，设置锁并设置锁过期时间，即加锁
                if (atomicLock(expireTime, unit)) {
                    this.lock = true;
                    return true;
                }

                // 线程睡眠
                // 按次数最多获取10次锁
                Thread.sleep(sleepTime);
            }
            while (System.currentTimeMillis() - beginTime < maxWaitTime);
        } catch (Exception e) {
            throw new RuntimeException("locking error");
        }
        return false;
    }

    public boolean lock(long maxWaitTime, long expireTime) {
        return lock(maxWaitTime, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 线程等待时间
     *
     * @param maxWaitTime
     * @return
     */
    private long calSleepTime(long maxWaitTime) {
        long sleepTime = maxWaitTime / times;
        if (sleepTime < minSleepTime) {
            return minSleepTime;
        } else {
            return sleepTime;
        }
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

    public boolean lock(long keyExpireTime) {
        return lock(0, keyExpireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 原子加锁
     *
     * @return
     */
    private boolean atomicLock(long expireTime, final TimeUnit unit) {
        // 转换格式
        expireTime = unit.toMillis(expireTime);

        // 正确返回1 错误返回0
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setScriptText("if redis.call('setnx',KEYS[1],ARGV[1])==1 then return redis.call('pexpire',KEYS[1],ARGV[2]) else return 0 end");
        script.setResultType(List.class);

        List rtn = (List) redisTemplate.execute(script, Lists.newArrayList(this.key), this.uuid, String.valueOf(expireTime));
        if (CollectionUtils.isNotEmpty(rtn)) {
            if (1 == Integer.valueOf(rtn.get(0).toString())) {
                return true;
            }
        }
        return false;
    }


    private boolean atomicLockJedis(long expireTime, final TimeUnit unit) {
        // 转换格式
        expireTime = unit.toMillis(expireTime);
        // NX setnx
        // PX ms
        //        Jedis jedis = redisConnection.getJedis();
        //        String result = jedis.set(this.key, this.uuid, "NX", "PX", expireTime);


        //        redisTemplate.execute()
        //        if ("OK".equals(result)) {
        //            this.lock = true;
        //            return true;
        //        }
        return false;
    }


    /**
     * 一般加锁
     *
     * @return
     */
    private boolean commonLock(long expireTime, final TimeUnit unit) {
        if (this.redisTemplate.opsForValue().setIfAbsent(this.key, this.uuid)) {
            // 设置锁失效时间, 防止永久阻塞
            this.redisTemplate.expire(key, expireTime, unit);
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
    private boolean commonUnlock() {
        Object o = redisTemplate.opsForValue().get(key);
        if (o != null && (o).equals(this.uuid)) {
            return redisTemplate.delete(key);
        }
        return false;
    }

    /**
     * 释放锁
     * 使用eval表达式 执行原子操作
     */
    private boolean atomicUnlock() {
        // 正确返回1 错误返回0
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
        script.setResultType(List.class);

        List rtn = (List) redisTemplate.execute(script, Lists.newArrayList(this.key), this.uuid);
        if (CollectionUtils.isNotEmpty(rtn)) {
            if (1 == Integer.valueOf(rtn.get(0).toString())) {
                return true;
            }
        }
        return false;
    }

    private void atomicUnlockJedis() {
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        // 正确返回1 错误返回0
//        Jedis jedis = redisConnection.getJedis();
//        Object result = jedis.eval(script, Collections.singletonList(this.key), Collections.singletonList(this.uuid));
//        jedis.close();
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
