package com.sun.instantiation;

import com.sun.util.UserFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sun
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public ServiceListFactoryBean serviceListFactoryBean() {
        ServiceListFactoryBean serviceListFactoryBean = new ServiceListFactoryBean();
        serviceListFactoryBean.setServiceType(UserFactory.class);
        return serviceListFactoryBean;
    }
}
