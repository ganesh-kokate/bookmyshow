package com.bookmyshow.booking;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.bookmyshow.booking", "com.bookmyshow.common"})
@EntityScan(basePackages = {"com.bookmyshow.common.models", "com.bookmyshow.booking"})
@EnableJpaRepositories(basePackages = {"com.bookmyshow.booking.repository"})
public class BookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
