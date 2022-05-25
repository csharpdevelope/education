package uz.example.flower.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfig {

    private final MinioConfigurationProperties minioConfigurationProperties;

    public MinioConfig(MinioConfigurationProperties minioConfigurationProperties) {
        this.minioConfigurationProperties = minioConfigurationProperties;
    }

    @Bean
    @Primary
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioConfigurationProperties.getUrl())
                .credentials(minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                .build();
    }
}
