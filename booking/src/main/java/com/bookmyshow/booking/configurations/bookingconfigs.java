package com.bookmyshow.booking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class bookingconfigs {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
