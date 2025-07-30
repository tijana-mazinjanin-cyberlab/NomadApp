package com.example.demo;

import Repositories.AccommodationRepository;
import Repositories.ReservationDateRepository;
import com.beust.ah.A;
import com.ibm.icu.impl.Assert;
import model.Accommodation;
import model.Reservation;
import model.ReservationDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class ReservationDateRepositoryTest {
    @Autowired
    ReservationDateRepository reservationDateRepository;

    @MockBean
    AccommodationRepository accommodationRepository;

    @ParameterizedTest
    @MethodSource("provideValidDates")
    public void shouldSaveValidDates(Date date){
        int size = reservationDateRepository.findAll().size();
        ReservationDate rDate = new ReservationDate();
        ReservationDate savedDate = new ReservationDate();
        rDate.setDate(date);
        rDate.setReservation(new Reservation());
        savedDate = reservationDateRepository.save(rDate);
        Assertions.assertEquals(size + 1, reservationDateRepository.findAll().size());
        Assertions.assertNotNull(savedDate);
        Assertions.assertNotEquals(0, savedDate.getId());
    }

    private static Stream<Arguments> provideValidArguments() {
        return Stream.of(
                Arguments.of(5L, 15.0),
                Arguments.of(6L, 25),
                Arguments.of(7L, 39)
        );
    }
    private static Stream<Arguments> providePrices() {
        return Stream.of(
                Arguments.of(15.0),
                Arguments.of(25),
                Arguments.of(39)
        );
    }
    private static Stream<Arguments> provideInvalidPrices() {
        return Stream.of(
                Arguments.of(-15.0),
                Arguments.of(-25),
                Arguments.of(-39)
        );
    }
    private static Stream<Arguments> provideInvalidDates() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return Stream.of(
                Arguments.of(formatter.parse("01-01-2002")),
                Arguments.of(formatter.parse("01-01-1700")),
                Arguments.of(formatter.parse("01-01-1990"))
        );
    }
    private static Stream<Arguments> provideValidDates() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return Stream.of(
                Arguments.of(formatter.parse("01-01-2025")),
                Arguments.of(formatter.parse("01-01-2500")),
                Arguments.of(formatter.parse("01-01-2026"))
        );
    }

    public Stream<Arguments> provideValidDatesAndAccommodations() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return Stream.of(
                Arguments.of(formatter.parse("01-01-2025"), 12L),
                Arguments.of(formatter.parse("01-01-2500"), 13L),
                Arguments.of(formatter.parse("01-01-2026"), 14L)
        );
    }
}
