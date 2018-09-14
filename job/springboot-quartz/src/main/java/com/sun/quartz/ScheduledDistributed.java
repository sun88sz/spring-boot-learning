package com.sun.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author : Sun
 * @date : 2018/9/14 14:49
 */
@Slf4j
public class ScheduledDistributed {

    SimpleDateFormat dateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private RedissonClient redissonClient;

    public ScheduledDistributed(String cron) {
        this.cron = cron;
    }

    private final String cron;

    /**
     * 每10s执行一次
     */
    @Scheduled(cron = "${scheduled.cron.A}")
    public void distributeCal() {
        log.info("Scheduling Started, The time is now {}", dateFormatFull.format(new Date()));

        Date nextTime = getNextTriggerTime(cron);
        if (nextTime != null) {
            String format = dateFormat.format(nextTime);
            log.info("Scheduling Next time is {}", format);

            RLock lock = redissonClient.getLock("SCHEDULED:A");

            // 这里如果无法获取锁，一定是有其他的job运行了，所以没必要重试
//            boolean b = lock.lock();
//            if (b) {
//                lock.
//                // 执行业务逻辑----------------------------------------------------
//                // 执行业务逻辑----------------------------------------------------
//                log.info("执行业务逻辑");
//            }
//            lock.unlock();
        }
    }


    public static Date getNextTriggerTime(String cron) {
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 这个是重点，一行代码搞定
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);

            if (CollectionUtils.isNotEmpty(dates)) {
                return dates.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
