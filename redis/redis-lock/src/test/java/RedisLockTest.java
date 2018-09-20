import com.google.common.collect.Lists;
import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisLockTestApplication.class)
public class RedisLockTest {

    @Autowired
    private RedisLockManager redisLockManager;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void redisLockTest() {
        RedisLock redisLock = redisLockManager.getRedisLock("Job:StatsQuality");
        boolean lock = redisLock.lock(30, TimeUnit.SECONDS);
        redisLock.unlock();

        RedisLock redisLock2 = redisLockManager.getRedisLock("Job:StatsQuality");
        boolean lock1 = redisLock2.lock(30, TimeUnit.SECONDS);

        redisLock2.unlock();
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
