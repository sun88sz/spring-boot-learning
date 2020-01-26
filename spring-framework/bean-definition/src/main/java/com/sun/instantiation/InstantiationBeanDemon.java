package com.sun.instantiation;

import com.sun.bean.Company;
import com.sun.bean.User;
import com.sun.util.UserFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 实例化Bean
 *
 * @author Sun
 */
public class InstantiationBeanDemon {

    public static void main(String[] args) throws Exception {

        /**
         * 1. Java ServiceLoader
         * 这是来自JDK的开箱即用的Java API，它提供了一种特定方式的控制反转IOC，由Service Loader类实现。
         * 它用来定位类路径上的某个接口的实现类。
         * 这种方式能够让我们在Java运行时动态发现类路径classpath上指定接口的某个实现，动态工厂加载模式，这样就分离了API模块和它的多个具体实现模块类。
         * 也就是说，同一套API接口，我们后面可以给予它不同的实现方式，这种接口和实现的清晰分离一直是Java生态最强的优势。
         */
        // 从 META-INF/services 中load类
        // 在这个目录里有一个文件，文件的名称完全是接口的完整路径名称，而其内容则是指定接口的实现类完整路径名称
        ServiceLoader<UserFactory> load = ServiceLoader.load(UserFactory.class, Thread.currentThread().getContextClassLoader());
        load.stream().map(ServiceLoader.Provider::get).map(UserFactory::createUser).forEach(System.out::println);


        /** 2. ServiceLoaderFactoryBean
         * Java ServiceLoader 与 Spring的结合
         */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ServiceConfiguration.class);
        context.refresh();

        ServiceListFactoryBean bean = context.getBean(ServiceListFactoryBean.class);

        System.out.println(bean);
        Object object = bean.getObject();

        System.out.println(object);
        // [com.sun.util.DefaultUserFactory@740fb309, com.sun.util.DefaultUserFactory2@7bd7d6d6]


        /** 3.Spring Factories Loader
         * Spring 的扩展
         * 与Java Service Loader一样，Spring提供了另一个反转控制的实现，但是只涉及一个属性文件，
         * 它必须被命名spring.factories并位于Jar其下META-INF目录下。
         * 从代码的角度来看，该文件是通过SpringFactoriesLoader.loadFactories() 这个静态方法读取
         */
        /**
         * 与Java Service Loader相比，差异有两方面：
         * 1. 一种文件格式是否比其他文件格式更好 ? 更可读或更易于维护，这也许是个人品味的问题。
         * 2. spring.factories的关键是不需要接口，也不需要实现子类来实现它。
         * Spring Boot就是使用这种方法来处理自动配置bean：关键就只是一个注释，即 org.springframework.boot.autoconfigure.EnableAutoConfiguration，
         * 值是在类上面的注解 @Configuration中写明的，这能够有更灵活的设计。
         */
        // 第二个参数是可选的类加载器
//        List<UserFactory> UserFactories = SpringFactoriesLoader.loadFactories(UserFactory.class, null);

        /**
         * 3.AutowireCapableBeanFactory
         */
        AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
        Company bean1 = autowireCapableBeanFactory.createBean(Company.class);
        System.out.println(bean1);
//        System.out.println(context.getBean(Company.class));
    }


}

