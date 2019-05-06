package com.sun.test.test;

import java.time.LocalTime;

public class LocalTimeTest {

    public static void main(String[] args) {

        // LocalTime只包含时间，以前用java.util.Date怎么才能只表示时间呢？答案是，假装忽略日期。
        // LocalTime包含纳秒：
        LocalTime now = LocalTime.now(); // 11:09:09.160345500
        System.out.println(now);

        // 你可能想清除毫秒数：
        LocalTime now2 = LocalTime.now().withNano(0); // 11:09:09
        System.out.println(now2);

        // 构造时间也很简单：
        LocalTime x = LocalTime.of(11, 0, 59); // 00:00:00
        System.out.println(x);

        LocalTime y = LocalTime.parse("12:31:00"); // 12:00:00
        System.out.println(y);


    }
}
