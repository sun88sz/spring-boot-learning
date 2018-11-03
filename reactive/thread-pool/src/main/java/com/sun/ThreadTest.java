package com.sun;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 
 * @author Sun
 */
public class ThreadTest {
    /**
     * LinkedBlockingQueue
     * 这个队列接收到任务的时候，如果当前线程数小于核心线程数，则新建线程(核心线程)处理任务；如果当前线程数等于核心线程数，则进入队列等待。
     * 由于这个队列没有最大值限制，即所有超过核心线程数的任务都将被添加到队列中，这也就导致了maximumPoolSize的设定失效，因为总线程数永远不会超过corePoolSize
     */
    private static ThreadPoolExecutor executorLinked = new ThreadPoolExecutor(2, 3, 20, TimeUnit.SECONDS,
            new LinkedBlockingQueue());

    /**
     * SynchronousQueue
     * 这个队列接收到任务的时候，会直接提交给线程处理，而不保留它，如果所有线程都在工作怎么办？那就新建一个线程来处理这个任务！
     * 所以为了保证不出现<线程数达到了maximumPoolSize而不能新建线程>的错误，
     * 使用这个类型队列的时候，maximumPoolSize一般指定成Integer.MAX_VALUE，即无限大
     */
    private static ThreadPoolExecutor executorSynchronous = new ThreadPoolExecutor(2, 3, 20, TimeUnit.SECONDS,
            new SynchronousQueue());

    /**
     * ArrayBlockingQueue
     * 可以限定队列的长度，接收到任务的时候，如果没有达到corePoolSize的值，则新建线程(核心线程)执行任务，
     * 如果达到了，则入队等候，如果队列已满，则新建线程(非核心线程)执行任务，
     * 又如果总线程数到了maximumPoolSize，并且队列也满了，则发生错误
     */
    private static ThreadPoolExecutor executorArray = new ThreadPoolExecutor(2, 3, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue(4));

    /**
     * 等同于
     */
//    private static ThreadPoolExecutor executorLinked = new ThreadPoolExecutor(2, 4, 20, TimeUnit.SECONDS,
//            new LinkedBlockingQueue(4));


    /**
     * 
     * @param executor
     */
    private static void testExecutor(ThreadPoolExecutor executor){
        for (int i = 0; i < 15; i++) {
            int index = i;
            executor.submit(
                    () -> {
                        Thread thread = Thread.currentThread();
                        String d1 = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
                        System.out.println(thread.getName() + "  No." + index + " start : " + d1);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String d2 = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
                        System.out.println(thread.getName() + "  No." + index + " end   : " + d2);
                    }
            );
        }
    }
    
    
    public static void main(String[] args) {
        // Queue无限，不舍弃，每次运行 corePoolSize 个线程
        testExecutor(executorLinked);
        
        // 超出 maxPoolSize 的直接报错，每次运行 maxPoolSize 个线程
//        testExecutor(executorSynchronous);
        
        // 超出 maxPoolSize + QueueSize 的直接报错，每次运行 maxPoolSize 个线程
//        testExecutor(executorArray);
    }
}
