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
public class JobTest2 {


    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    /**
     * 每30s执行一次
     */
    @SynJob(jobName = "JOB1")
    @Scheduled(cron = "0/10 * * * * ?")
    public void reportCurrentByCron() {
        System.out.println("JOB2 - " + sdf.format(SynJobAspect.getNextTriggerTime("0/10 * * * * ?")));
    }


}
