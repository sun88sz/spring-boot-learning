package com.sun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : Sun
 * @date : 2018/9/13 19:36
 */
@EnableScheduling
@SpringBootApplication
public class SynJobTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynJobTestApplication.class);
    }
}
