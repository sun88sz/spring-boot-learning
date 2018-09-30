package com.sun;

import reactor.core.publisher.Flux;

/**
 * @author : Sun
 * @date : 2018/9/30 14:32
 */
public class ReactorTest {
    
    public static void main(String[] args) {
        
        Flux.range(1,1000000).subscribe(System.out::println);

        System.out.println("--------------");
    }
}
