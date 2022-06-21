package uz.example.flower;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@SpringBootApplication
@SecurityScheme(name = "Flower Shop", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
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
