package com.sun.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Sun
 */
public class ScheduledTest2 {

//    @Scheduled(fixedRate = 1000 * 30)
    public void reportCurrentTime() {
        System.out.println("Scheduling Tasks Examples: The time is now " + dateFormat().format(new Date()));
    }

    /**
     * 每10s执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void reportCurrentByCron() {
        System.out.println("Scheduling Tasks Examples By Cron: The time is now " + dateFormat().format(new Date()));
        
        List<Date> times = getRecentTriggerTime("0/10 * * * * ?");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : times) {
            System.out.println(dateFormat.format(date));
        }
    }

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }


    public static List<Date> getRecentTriggerTime(String cron) {
        List<Date> dates = null;
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 这个是重点，一行代码搞定
            dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);
            
            return dates;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

    
    

    public static void main(String[] args) {
        List<Date> times = getRecentTriggerTime("0 * * * * ?");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : times) {
            System.out.println(dateFormat.format(date));
        }
    }
}