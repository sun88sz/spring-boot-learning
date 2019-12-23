package com.sun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: <br/>
 * Date: 2019-05-10
 *
 * @author Sun
 */
@EnableSwagger2
@SpringBootApplication
public class BeanValidationCustomizeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeanValidationCustomizeApplication.class);
    }
}
