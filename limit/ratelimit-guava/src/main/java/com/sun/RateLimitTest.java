package com.sun;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 限流
 */
public class RateLimitTest {

    /**
     * 首先通过RateLimiter.create(1);
     * 创建一个限流器，参数代表每秒生成的令牌数，通过limiter.acquire(i);来以阻塞的方式获取令牌，
     * 当然也可以通过tryAcquire(int permits, long timeout, TimeUnit unit)来设置等待超时时间的方式获取令牌，
     * 如果超timeout为0，则代表非阻塞，获取不到立即返回。
     */
    public void testAcquire() {
        // 每秒产生一个令牌
        final RateLimiter limiter = RateLimiter.create(1);

        for (int i = 1; i < 10; i = i + 2) {
            // 获取需要几个量
            // RateLimiter支持预消费，比如在acquire(5)时，等待时间是3秒，是上一个获取令牌时预消费了3个令牌，固需要等待3*1秒，然后又预消费了5个令牌，以此类推
            double waitTime = limiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    // SmoothBuisty
//    SleepingStopwatch：guava中的一个时钟类实例，会通过这个来计算时间及令牌
//    maxBurstSeconds：官方解释，在RateLimiter未使用时，最多保存几秒的令牌，默认是1


    public static void main(String[] args) {
        RateLimitTest rateLimitTest = new RateLimitTest();
        rateLimitTest.testAcquire();
    }
}
