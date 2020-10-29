package com.wind;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BackendServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BackendServiceApplication.class);
        application.setBannerMode(Banner.Mode.LOG);
        application.run(args);
    }

}
