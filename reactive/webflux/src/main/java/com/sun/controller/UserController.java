package com.sun.controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author : Sun
 * @date : 2018/10/8 10:00
 */
@RestController
@Slf4j
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/reactive")
    public Mono<String> reactive() {
        return Mono.just("Welcome to reactive world");
    }


    /**
     * 此处不支持 GetMapping
     * @param serverRequest
     * @return
     */
    //    @GetMapping("/time")
    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {

        log.info("attributes");
        serverRequest.attributes().forEach((k, v) -> log.info(k + " : " + v));

        log.info("queryParams");
        serverRequest.queryParams().forEach((k, v) -> log.info(k + " : " + v));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
                Flux.interval(Duration.ofSeconds(1))
                        .map(l -> simpleDateFormat.format(new Date())), String.class);
    }

}

