import com.sun.RedisLockConfiguration;
import com.sun.lock.RedisLock;
import com.sun.lock.RedisLockManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Description: <br/>
 * Date: 2018-09-16
 *
 * @author Sun
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisLockConfiguration.class)
public class RedisLockTest {

    @Autowired
    private RedisLockManager redisLockManager;

    @Test
    public void redisLockTest() {
//        RedisLock redisLock = redisLockManager.getRedisLock("Job:StatsQuality");
//        boolean lock = redisLock.lock(30, TimeUnit.SECONDS);


        RedisLock redisLock2 = redisLockManager.getRedisLock("Job:StatsQuality");
        boolean lock1 = redisLock2.lock(30, TimeUnit.SECONDS);

        redisLock2.unlock();
    }
}
