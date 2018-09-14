package com.sun.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author : Sun
 * @date : 2018/9/14 10:19
 */
public class ScheduledTest1 {


//    schedule(TimerTask task, Date firstTime, long period)
//    安排指定的任务在指定的时间开始进行重复的固定延迟执行。
//
//    scheduleAtFixedRate(TimerTask task, Date firstTime, long period)
//    安排指定的任务在指定的时间开始进行重复的固定速率执行。
//    scheduleAtFixedRate(TimerTask task, long delay, long period)
//    安排指定的任务在指定的延迟后开始进行重复的固定速率执行。

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    @Scheduled(cron = "0/1 * * * * ?")
    @Async
    public void everySecAsync() {
        printDate();
    }

//    @Scheduled(cron = "0/1 * * * * ?")
//    public void everySec() {
//        printDate();
//    }

//    /**
//     * 上一个任务执行完成之后，再过fixedDelay毫秒后，执行
//     */
//    @Scheduled(fixedDelay = 1000)
//    public void delaySecs() {
//        printDate();
//    }
//
//    /**
//     * 
//     */
//    @Scheduled(fixedRate = 1000)
//    public void everySecs() {
//        printDate();
//    }


    public void printDate() {
        System.out.println(dateFormat.format(new Date()) + "  Begin  :  " +Thread.currentThread().getName());
        try {
            Thread.sleep(7100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(dateFormat.format(new Date()) +"  E n d  :  "+Thread.currentThread().getName() );
    }

}
