import com.sun.IntegrationLockApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IntegrationLockApplication.class)
public class LockTest {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Test
    public void test() throws InterruptedException {
        Lock lock = redisLockRegistry.obtain("lock");

        boolean b1 = lock.tryLock(3, TimeUnit.SECONDS);
        log.info("b1 is : {}", b1);

        TimeUnit.SECONDS.sleep(10);

        lock.unlock();
    }


    @Test
    public void test2() throws InterruptedException {
        Lock lock = redisLockRegistry.obtain("lock");

        boolean b1 = lock.tryLock(1, TimeUnit.SECONDS);
        log.info("b1 is : {}", b1);

        lock.unlock();
    }


    @Test
    public void test3() throws InterruptedException {
        Lock lock = redisLockRegistry.obtain("lock");

        boolean b1 = lock.tryLock(10, TimeUnit.SECONDS);
        log.info("b1 is : {}", b1);

        TimeUnit.SECONDS.sleep(3);

        boolean b2 = lock.tryLock(10, TimeUnit.SECONDS);
        log.info("b2 is : {}", b2);

        lock.unlock();
        lock.unlock();
    }
}
