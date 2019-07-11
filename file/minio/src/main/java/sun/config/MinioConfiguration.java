package sun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.service.MinioFileService;

@Configuration
public class MinioConfiguration {

    @Bean
    public MinioFileService minioFile() {
        return new MinioFileService();
    }
}
