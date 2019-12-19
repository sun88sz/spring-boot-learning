package com.sun.controller;

import io.prometheus.client.Counter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 *
 * @author Sun
 */
@RestController
public class SampleController {

    private static Random random = new Random();

    private static final Counter requestTotal = Counter.build()
        .name("my_sample_counter")
        .labelNames("status")
        .help("A simple Counter to illustrate custom Counters in Spring Boot and Prometheus").register();

    @RequestMapping("/endpoint")
    public void endpoint() {
        if (random.nextInt(2) > 0) {
            requestTotal.labels("success").inc();
        } else {
            requestTotal.labels("error").inc();
        }
    }


    @GetMapping("/test")
    public String test() {
        return "xxxxxxxxxxxxxxxxxx";
    }

}