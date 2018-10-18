import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

/**
 * @author : Sun
 * @date : 2018/10/11 10:02
 */
public class Pool {

    static TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();
    static ExecutorService pool =  TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));
    
    
    
}
