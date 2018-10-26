import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author : Sun
 * @date : 2018/9/30 14:53
 */
@Slf4j
public class FluxTest {
    
    @Test
    public void baseSubscribe(){
        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .filter(i -> i > 2)
                .subscribe(
                        new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                log.info("onSubscribe");
                                s.request(10);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                log.info("onNext : {}", integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                log.info("onError : {}", t);
                            }

                            @Override
                            public void onComplete() {
                                log.info("onComplete");
                            }
                        }
                );
    }


    @Test
    public void base() {
        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .filter(i -> i > 2)
                .map(i -> String.valueOf(i))
                .subscribe(log::info);
    }
    


    @Test
    public void xx() throws InterruptedException {
        System.out.println("--");

        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2

        Flux.just("flux", "mono")
                .flatMap(s -> Flux.fromArray(s.split("\\s*"))
                        .delayElements(Duration.ofMillis(100)))
                .doOnNext(e-> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                })
                .subscribe(System.out::println);

//        countDownLatch.await(3,TimeUnit.SECONDS);

        System.out.println("end");
    }

    @Test
    public void yy() {
        System.out.println("--");
        
        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                                .delayElements(Duration.ofMillis(1000))) // 2
                        .doOnNext(System.out::print)) // 3
                .expectNextCount(8) // 4
                .verifyComplete();
    }
}
