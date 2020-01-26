package com.sun;

import com.sun.bean.User;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Sun
 */
@SpringBootApplication
public class SpringBeanApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBeanApplication.class);

        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
        beanFactory.getBean(User.class);

    }
}
