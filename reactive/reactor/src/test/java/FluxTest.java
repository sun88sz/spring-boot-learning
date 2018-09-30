import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author : Sun
 * @date : 2018/9/30 14:53
 */
public class FluxTest {


    @Test
    public void xx() throws InterruptedException {
        System.out.println("--");

        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2

        Flux.just("flux", "mono")
                .flatMap(s -> Flux.fromArray(s.split("\\s*"))
                        .delayElements(Duration.ofMillis(100)))
                .doOnNext(System.out::print)
                .subscribe(System.out::print);

        countDownLatch.await(1,TimeUnit.SECONDS);
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
