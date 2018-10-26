package com.sun;

import com.google.common.collect.Lists;
import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisLockTestApplication.class)
public class RedisLockTest {

    @Autowired
    private RedisLockManager redisLockManager;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void redisLockTest1() throws InterruptedException {
        RedisLock redisLock = redisLockManager.getRedisLock("AAAA:BBBB");
        
        log.info( "1 try lock");
        boolean lock = redisLock.lock(10,30, TimeUnit.SECONDS);
        log.info( "1 get lock");
        Thread.sleep(10000);
        if(lock) {
            redisLock.unlock();
            log.info( "1 unlock lock");
        }

    }


    @Test
    public void redisLockTest2() {
        RedisLock redisLock = redisLockManager.getRedisLock("AAAA:BBBB");

        log.info( "2 try lock");
        boolean lock = redisLock.lock(10,30, TimeUnit.SECONDS);
        log.info( "2 get lock");
        if(lock) {
            redisLock.unlock();
            log.info( "2 unlock lock");
        }

    }



    @Test
    public void lockTest() {
        // 正确返回1 错误返回0
        DefaultRedisScript<List> x = new DefaultRedisScript<>();

        x.setScriptText("if redis.call('setnx',KEYS[1],ARGV[1])==1 then return redis.call('pexpire',KEYS[1],ARGV[2]) else return 0 end");
        x.setResultType(List.class);

        Object execute = redisTemplate.execute(x, Lists.newArrayList("xxxx"), "yyyy", "20000");

        System.out.println(execute);
    }
    
    
    @Test
    public void unlockTest() {
        // 正确返回1 错误返回0

        DefaultRedisScript<List> x = new DefaultRedisScript<>();

        x.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
        x.setResultType(List.class);

        Object execute = redisTemplate.execute(x, Lists.newArrayList("xx"), "yy");

//        Object execute = redisTemplate.execute(
//                x,  seri,seri, Lists.newArrayList("xx"), "yy"
////                x, Lists.newArrayList(), Lists.newArrayList()
//        );


        System.out.println(execute);
    }




}
