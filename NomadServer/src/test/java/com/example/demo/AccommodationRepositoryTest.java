package com.example.demo;

import Repositories.AccommodationRepository;
import model.Accommodation;
import model.Reservation;
import model.ReservationDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AccommodationRepositoryTest {
    @Autowired
    AccommodationRepository accommodationRepository;

    @RepeatedTest(3)
    public void shouldSaveAccommodation(){
        int size = accommodationRepository.findAll().size();
        Accommodation accommodation = new Accommodation();
        Accommodation savedAccommodation = new Accommodation();
        accommodation.setAddress("AAAAA");
        accommodation.setName("AAAAA");
        accommodation.setDescription("AAAAA");
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(5);
        accommodation.setDefaultPrice(10);

        savedAccommodation = accommodationRepository.save(accommodation);
        Assertions.assertEquals(size + 1, accommodationRepository.findAll().size());
        Assertions.assertNotNull(savedAccommodation);
        Assertions.assertNotEquals(0, savedAccommodation.getId());
    }
    @ParameterizedTest
    @MethodSource("provideNames")
    public void shouldUpdateAccommodation(String name){
        Accommodation accommodation = new Accommodation();
        accommodation.setAddress("AAAAA");
        accommodation.setName("AAAAA");
        accommodation.setDescription("AAAAA");
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(5);
        accommodation.setDefaultPrice(10);
        accommodationRepository.save(accommodation);
        int size = accommodationRepository.findAll().size();
        Accommodation savedAccommodation = accommodationRepository.findOneById(accommodation.getId());
        savedAccommodation.setName(name);
        accommodationRepository.save(savedAccommodation);

        Assertions.assertEquals(size , accommodationRepository.findAll().size());
        Assertions.assertNotNull(savedAccommodation);
        Assertions.assertNotEquals("AAAA", accommodationRepository.findOneById(accommodation.getId()).getName());
        Assertions.assertEquals(name, accommodationRepository.findOneById(accommodation.getId()).getName());
    }

    private static Stream<Arguments> provideNames() {
        return Stream.of(
                Arguments.of("Name1"),
                Arguments.of("Name2"),
                Arguments.of("Name3")
        );
    }
}
