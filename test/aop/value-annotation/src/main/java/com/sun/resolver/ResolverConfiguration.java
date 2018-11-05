package com.sun.resolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Sun
 * @date : 2018/11/5 16:55
 */
@Configuration
public class ResolverConfiguration {


    @Bean
    public StringValueResolverRegistrar stringValueResolverRegister() {
        return new StringValueResolverRegistrar();
    }

    @Bean
    public CustomizedStringValueResolver customizedBeanFactoryUtils() {
        return new CustomizedStringValueResolver();
    }

}
