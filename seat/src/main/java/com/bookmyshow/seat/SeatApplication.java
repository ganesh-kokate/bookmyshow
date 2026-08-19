package com.bookmyshow.seat;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.bookmyshow.seat", "com.bookmyshow.common"})
@EntityScan(basePackages = {"com.bookmyshow.common.models", "com.bookmyshow.seat"})
@EnableJpaRepositories(basePackages = {"com.bookmyshow.seat.repository"})
@EnableScheduling
public class SeatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeatApplication.class, args);
    }
}
