package com.sun;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Sun
 * @date : 2018/10/8 17:13
 */
@Slf4j
public class RxJavaTest {

    public static void main(String[] args) {
        Observable<String> obs = Observable.create(e -> {
            if (!e.isDisposed()) {
                e.onNext("A");
                log.info("String emit : A \n");

                e.onNext("B");
                log.info("String emit : B \n");
                
                e.onNext("C");
                log.info("String emit : C \n");
            }
        });
        
        obs.map(x -> x + x).subscribe(System.out::println);
    }
}
