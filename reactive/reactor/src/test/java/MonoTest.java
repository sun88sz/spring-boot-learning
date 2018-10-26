import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : Sun
 * @date : 2018/10/8 17:40
 */
@Slf4j
public class MonoTest {

    @Test
    public void monoTest() {
        System.out.println("--");

//        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2

        Flux<String> mono = Flux.just("mono");
//        mono = mono.flatMap(s -> Flux.fromArray(s.split("\\s*"))
        mono = mono.delayElements(Duration.ofMillis(1));


        mono.doOnNext(e -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        })
                .subscribe(System.out::println);

//        countDownLatch.await(3,TimeUnit.SECONDS);

        System.out.println("end");
    }


    @Test
    public void mono2() {

        CountDownLatch countDownLatch = new CountDownLatch(3);  // 2
        countDownLatch.countDown();


        List<DepartmentResponse> list = new ArrayList<>();

        MonoTest monoTest = new MonoTest();

        Mono<Department> a = Mono.just(new Department(1L, "A")).doOnSuccess(x -> countDownLatch.countDown());
//        Mono<DepartmentResponse> b = Mono.just(new Department(2L, "B")).map(MonoTest::doSomething).doOnNext(x-> list.add(x)).doOnSuccess(x -> countDownLatch.countDown());
//        Mono<DepartmentResponse> c = Mono.just(new Department(3L, "C")).map(MonoTest::doSomething).doOnNext(x-> list.add(x)).doOnSuccess(x -> countDownLatch.countDown());

//        c.subscribe();
//        b.subscribe();

        System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " subscribe start");
        a.subscribe(monoTest::doSomething);
        System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " subscribe end");

//        Mono<DepartmentResponse> single = a.single();
//        a.subscribe(MonoTest::doSomething);
//        b.subscribe(MonoTest::doSomething);
//        c.subscribe(MonoTest::doSomething);


//        countDownLatch.await(10, TimeUnit.SECONDS);

        System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

        System.out.println(list);
    }


    public DepartmentResponse doSomething(Department department) {
        System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\t" + "Start");
        System.out.println(department);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//
//            e.printStackTrace();
//        }


        try {
            FileInputStream fis = new FileInputStream(new File("D:/AAAA.xlsx"));


            FileOutputStream fos = new FileOutputStream(new File("D:/" + System.currentTimeMillis() + ".xlsx"));

            byte[] x = new byte[1000000000];
            int read = fis.read(x, 0, 1000000000);
            fos.write(x);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\t" + "End");


        return new DepartmentResponse(department.getId() + 10, department.getName() + "_response");
    }


}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Department {
    Long id;
    String name;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class DepartmentResponse {
    Long iddd;
    String nameee;
}
