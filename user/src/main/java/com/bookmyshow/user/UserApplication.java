package com.bookmyshow.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.bookmyshow.user", "com.bookmyshow.common"})
@EntityScan(basePackages = {"com.bookmyshow.common.models", "com.bookmyshow.user"})
@EnableJpaRepositories(basePackages = {"com.bookmyshow.user.repository"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
