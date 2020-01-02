package com.sun.helpers;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(OfficeProperties.class)
@PropertySource("settings.properties")
public class OfficeSettingConfiguration {

    @Bean
    public OfficeProperties officeProperties(){
        return new OfficeProperties();
    }
}
