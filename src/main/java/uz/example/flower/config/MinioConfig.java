package uz.example.flower.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.access.key}")
    private String accessKey;
    @Value("${minio.secret.key}")
    private String secretKey;

    @Bean
    @Primary
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
