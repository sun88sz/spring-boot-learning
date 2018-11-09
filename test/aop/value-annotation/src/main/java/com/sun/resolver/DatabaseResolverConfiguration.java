package com.sun.resolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 常量解析启动配置
 *
 * @author : Sun
 * @date : 2018/11/5 16:55
 */
@Configuration
public class DatabaseResolverConfiguration {
    
    @Bean
    public StringValueResolverRegister beanFactoryPostProcessorResolver() {
        return new StringValueResolverRegister();
    }

    @Bean
    public DatabasePropertyPlaceholderConfigurer databasePropertyPlaceholderConfigurer() {

        DatabasePropertyPlaceholderConfigurer con = new DatabasePropertyPlaceholderConfigurer();
        con.setOrder(-1);
        con.setIgnoreUnresolvablePlaceholders(false);

        return con;
    }
}
