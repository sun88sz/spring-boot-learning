package com.sun.register;

import com.sun.util.DefaultUserFactory;
import com.sun.util.UserFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注册外部对象：生命周期不由spring管理
 * @author Sun
 */
public class OuterSingletonRegister {

    public static void main(String[] args) {


        // 注册外部对象：生命周期不由spring管理
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        UserFactory userFactory = new DefaultUserFactory();

        beanFactory.registerSingleton("userFactory", userFactory);

        context.refresh();
    }
}
