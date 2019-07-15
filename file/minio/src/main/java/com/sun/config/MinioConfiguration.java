package com.sun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sun.service.MinioFileService;

@Configuration
public class MinioConfiguration {

    @Bean
    public MinioFileService minioFileService() {
        return new MinioFileService();
    }
}
