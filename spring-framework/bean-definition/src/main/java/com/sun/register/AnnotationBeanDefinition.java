package com.sun.register;

import com.sun.bean.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Sun
 */
@Import(AnnotationConfiguration3.class)
public class AnnotationBeanDefinition {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 1. 通过 @Bean
        context.register(AnnotationConfiguration1.class);

        // 2. 通过 @Component
        // @Compontent只能用于类上，把 AnnotationConfiguration2 注册为一个Bean
        context.register(AnnotationConfiguration2.class);

        // 3. 通过@Import
        // 在 AnnotationBeanDefinition 上添加 @Import(AnnotationConfiguration3.class)
        // 类似于直接使用register注册对应类，但可以整合
        context.register(AnnotationBeanDefinition.class);

        context.refresh();

        Map<String, AnnotationConfiguration2> beansOfType = context.getBeansOfType(AnnotationConfiguration2.class);
        beansOfType.entrySet().stream().forEach(
                e -> {
                    System.out.println(e.getKey() + " : " + e.getValue());
                }
        );

        Map<String, User> beansOfType2 = context.getBeansOfType(User.class);
        beansOfType2.entrySet().stream().forEach(
                e -> {
                    System.out.println(e.getKey() + " : " + e.getValue());
                }
        );


        context.close();
    }

}


class AnnotationConfiguration1 {
    @Bean(name = {"user", "aliesUser"})
    public User getUser() {
        return User.builder().age(10).name("xxx").build();
    }
}


@Component
class AnnotationConfiguration2 {

    public User getUser() {
        return User.builder().age(10).name("xxx").build();
    }
}

class AnnotationConfiguration3 {
    @Bean(name = {"user", "aliesUser"})
    public User getUser() {
        return User.builder().age(10).name("xxx").build();
    }
}
