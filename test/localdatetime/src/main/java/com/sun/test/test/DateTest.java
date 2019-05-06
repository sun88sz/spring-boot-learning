package com.sun.test.test;

import java.util.Calendar;
import java.util.Date;

public class DateTest {

    public static void main(String[] args) {

//        获取日期的年月日
//        代替已废弃的Date.getDay()之类的方法
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int day = c.get(Calendar.DAY_OF_MONTH);
        System.out.println(day);

        int month = c.get(Calendar.MONTH) + 1;
        System.out.println(month);

        int year = c.get(Calendar.YEAR);
        System.out.println(year);


        // +1天
        Date date = new Date();
        System.out.println(date);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        c2.add(Calendar.DAY_OF_YEAR, 1);
        Date date2 = c2.getTime();

        System.out.println(date2);

    }
}
