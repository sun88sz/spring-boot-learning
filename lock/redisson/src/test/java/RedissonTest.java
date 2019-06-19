import com.sun.redisson.RedissonApplication;
import com.sun.redisson.example.LockExampleService;
import com.sun.redisson.example.LockParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author : Sun
 * @date : 2018/12/20 17:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedissonApplication.class)
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void lock() throws InterruptedException {
        RLock lock = redissonClient.getLock("xxxx");
        boolean b = lock.tryLock(10, TimeUnit.SECONDS);

        System.out.println(b);
    }


    @Autowired
    private LockExampleService lockExampleService;

    @Test
    public void testAspect() {
        LockParameter parameter = new LockParameter();
        parameter.setId(1L);
        parameter.setName("xxxxxxxxx");

        lockExampleService.doing(parameter);
    }

}
