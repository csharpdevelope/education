package uz.example.flower;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.example.flower.config.MinioConfigurationProperties;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Collections;

@EnableRabbit
@SpringBootApplication
@SecurityScheme(name = "Flower Shop", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableConfigurationProperties(MinioConfigurationProperties.class)
public class FlowerApplication implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(FlowerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String hostAddress = "";
        InetAddress inetAddress = InetAddress.getLocalHost();
        hostAddress = inetAddress.getHostAddress();
        System.out.println("Your current ip-Address: " + hostAddress);
    }
}
