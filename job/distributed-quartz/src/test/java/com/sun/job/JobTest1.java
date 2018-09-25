package com.sun.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.synjob.SynJob;
import com.sun.synjob.SynJobAspect;

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
     * 每30s执行一次
     */
    @SynJob(jobName = "JOB1")
//    @Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "${scheduled.cron.test1}")
    public void reportCurrentByCron() {
        System.out.println("JOB1 - " + sdf.format(SynJobAspect.getNextTriggerTime("0/10 * * * * ?")));
    }

}
