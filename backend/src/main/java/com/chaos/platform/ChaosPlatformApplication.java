package com.chaos.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChaosPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChaosPlatformApplication.class, args);
    }
} 