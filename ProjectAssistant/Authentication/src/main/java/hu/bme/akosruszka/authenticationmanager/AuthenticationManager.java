package hu.bme.akosruszka.authenticationmanager;

import hu.bme.akosruszka.authenticationmanager.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AuthenticationManager {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationManager.class, args);
    }
}
