package com.sun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * @author : Sun
 * @date : 2018/10/22 18:49
 */
public class FluxTest {
    
    
    
    public void xx(){


        Flux.just(new Department(1,"A"),new Department(2,"B"),new Department(3,"C"));
    }
    
    
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Department {
    
    private Integer id;
    private String name;
}
