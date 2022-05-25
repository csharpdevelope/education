package uz.example.flower.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfigurationProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
