package com.sun;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 工作日计算工具类
 * 
 * @author Sun
 */
public class WorkDayUtil {

    /**
     * @param startTime
     * @param endTime
     * @return 相差的秒数
     */
    private static double getWorkdayTimeInSecs(Date startTime, Date endTime) {
        if (startTime.getTime() > endTime.getTime()) {
            Date x = startTime;
            startTime = endTime;
            endTime = x;
        }

        Date startWorkDay = getNextWorkDay(startTime);
        Date endWorkDay = getPrevWorkDay(endTime);

        // 周六 11:00 - 周日 9:00 就会出现错误
        if (startWorkDay.getTime() > endWorkDay.getTime()) {
            throw new IllegalArgumentException("开始日期工作日 晚于 结束日期工作日");
        }

        Date startFridayTail = getNextFridayTail(startWorkDay);
        Date endMondayHead = getPrevMondayHead(endWorkDay);

        // 此处一定可以余7一定=0
        double times = (endMondayHead.getTime() - startFridayTail.getTime() - 86400000 * 2) / 7.0 * 5;

        return (times + (startFridayTail.getTime() - startWorkDay.getTime()) + (endWorkDay.getTime() - endMondayHead.getTime())) / 1000;
    }

    /**
     * 下一个工作日
     * 时间平移
     * 周六 11:00 会 移动到 下周一 11:00
     *
     * @return
     */
    public static Date getNextWorkDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 2);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    /**
     * 上一个工作日
     * 时间平移
     * 周六 11:00 会 移动到 周五 11:00
     *
     * @return
     */
    public static Date getPrevWorkDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, -1);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -2);
        }
        return cal.getTime();
    }


    /**
     * 获取下一个周五 24点0分0秒  即周六0点0分0秒
     *
     * @param date
     * @return
     */
    public static Date getNextFridayTail(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int between = Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK);
        if (between != 0) {
            cal.add(Calendar.DATE, between);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取上一个周一  0点0分0秒
     *
     * @return
     */
    public static Date getPrevMondayHead(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int between = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (between != 0) {
            cal.add(Calendar.DATE, -between);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * 两个时间点 之间的天数差（精确到小数点后n位）
     *
     * @param begin
     * @param end
     * @param digit
     * @return
     */
    public static double betweenDaysOfDigit(Date begin, Date end, int digit) {
        Long beginTime = begin.getTime();
        Long endTime = end.getTime();
        double v = (Double.valueOf(endTime) - beginTime) / (1000 * 60 * 60 * 24);

        if (digit < 0) {
            throw new IllegalArgumentException("digit 不可为 负数");
        }

        BigDecimal between = new BigDecimal(v);
        between = between.setScale(digit + 1, RoundingMode.FLOOR);
        between = between.setScale(digit, RoundingMode.HALF_UP);

        return between.doubleValue();
    }

    /**
     * 两个时间点 之间的天数差（精确到小数点后n位）
     * 除去周末
     *
     * @param begin
     * @param end
     * @param digit
     * @return
     */
    public static double betweenDaysOfDigitExcludeWeekend(Date begin, Date end, int digit) {
        if (digit < 0) {
            throw new IllegalArgumentException("digit 不可为 负数");
        }
        double days = getWorkdayTimeInSecs(begin, end) / 86400.0;

        BigDecimal between = new BigDecimal(days);
        between = between.setScale(digit + 1, RoundingMode.FLOOR);
        between = between.setScale(digit, RoundingMode.HALF_UP);
        return between.doubleValue();
    }

}
