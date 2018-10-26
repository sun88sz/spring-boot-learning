package com.sun;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

/**
 * @author : Sun
 * @date : 2018/9/30 14:32
 */
@Slf4j
public class ReactorTest {

    public static void main(String[] args) {

        Flux<Integer> intFlux = Flux.just(1, 2, 3, 4, 5);
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E");

        intFlux.zipWith(stringFlux).subscribe(System.out::println);


//        intFlux.map(i -> i * 2).subscribe(System.out::print);
        

    }
}
