package com.sun.job;

import java.text.SimpleDateFormat;

import com.sun.synjob.SyncJob;
import com.sun.synjob.SyncJobAspect;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : Sun
 * @date : 2018/9/17 16:19
 */
@Component
public class JobTest1 {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    /**
     * 每5s执行一次
     */
    @SyncJob(jobName = "JOB1")
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "${scheduled.cron.test1}")
    public void reportCurrentByCron() {
        System.out.println("JOB1 - " + sdf.format(SyncJobAspect.getNextTriggerTime("0/5 * * * * ?")));
    }

}
