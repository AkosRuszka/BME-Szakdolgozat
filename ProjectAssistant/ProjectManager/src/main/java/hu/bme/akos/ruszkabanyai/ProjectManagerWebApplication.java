package hu.bme.akos.ruszkabanyai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class ProjectManagerWebApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProjectManagerWebApplication.class, args);
    }

}
