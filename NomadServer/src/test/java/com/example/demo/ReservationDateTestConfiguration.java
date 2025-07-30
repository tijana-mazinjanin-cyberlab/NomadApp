package com.example.demo;

import Repositories.ReservationDateRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ReservationDateTestConfiguration {
    @Bean
    @Primary
    public ReservationDateRepository reservationDateRepository() {
        return Mockito.mock(ReservationDateRepository.class);
    }
}

