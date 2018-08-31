package com.sun;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sun
 * @date : 2018/8/31 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReduceTest {

    int age;


    public static void main(String[] args) {
        
        ReduceTest reduce = Arrays.asList(new ReduceTest(1), new ReduceTest(2), new ReduceTest(4))
                .stream().reduce(new ReduceTest(), (a, b) -> (new ReduceTest(a.getAge() + b.getAge())));

        System.out.println(reduce.getAge());
    }
}
