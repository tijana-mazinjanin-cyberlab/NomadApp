package com.example.demo;

import Repositories.AccommodationRepository;
import Repositories.ReservationDateRepository;
import Services.AccommodationService;
import model.*;
import model.enums.AccommodationType;
import org.aspectj.lang.annotation.Before;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationDateTest {
    @MockBean
    private ReservationDateRepository reservationDateRepository;
    @MockBean
    private AccommodationRepository accommodationRepository;
    @Autowired
    @InjectMocks
    private AccommodationService accommodationService;
    //Use whenever price isn't a parameter of the test, but needs to be different from default
    private static double notDefaultPrice = 100;

    private static final Date date = new Date();

    @BeforeEach
    public void setUp() throws ParseException {
        date.setMonth(Calendar.DECEMBER);
        date.setYear(2025);
        ReservationDate resDate = new ReservationDate();
        Accommodation accommodation = new Accommodation();


        accommodation.setName("TESTNAME");
        accommodation.setDescription("TESTDESCRIPTION");
        accommodation.setAddress("TESTADDRESS");
        accommodation.setAccommodationType(AccommodationType.ROOM);
        accommodation.setHost(new Host());
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(2);
        accommodation.setDefaultPrice(10.0);
        resDate.setPrice(15.0);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(5L, date))
                .thenReturn(resDate);
        resDate.setPrice(25);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(6L, date))
                .thenReturn(resDate);
        resDate.setPrice(39);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(7L, date))
                .thenReturn(resDate);
        ReservationDate resDate2 = new ReservationDate();
        Reservation reservation = new Reservation();
        reservation.setUser(new Guest());
        resDate2.setReservation(reservation);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(8L, date))
                .thenReturn(resDate2);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(12L, formatter.parse("01-01-2025")))
                .thenReturn(resDate2);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(13L, formatter.parse("01-01-2500")))
                .thenReturn(resDate2);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(14L, formatter.parse("01-01-2026")))
                .thenReturn(resDate2);

        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(9L, date))
                .thenReturn(null);

        Mockito.when(accommodationRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(accommodation));
        Mockito.when(accommodationRepository.findById(9L)).thenReturn(null);
        Mockito.when(accommodationRepository.findById(19L)).thenReturn(null);
        Mockito.when(accommodationRepository.findById(20L)).thenReturn(null);
        Mockito.when(accommodationRepository.findOneById(Mockito.anyLong())).thenReturn(accommodation);
        Mockito.when(accommodationRepository.findOneById(9L)).thenReturn(null);
        Mockito.when(accommodationRepository.findOneById(19L)).thenReturn(null);
        Mockito.when(accommodationRepository.findOneById(20L)).thenReturn(null);

    }

    @RepeatedTest(5)
    public void testSetPriceWhenReserved() {
        Assertions.assertFalse(accommodationService.setPrice(8L, 15.0, date));
//        Assertions.assertEquals(accommodationService.getPrice(2L, date), 15.0);
    }

    @ParameterizedTest
    @MethodSource("provideValidArguments")
    public void testSetPriceValid(Long accommodationId, double price) {

        Assertions.assertTrue(accommodationService.setPrice(accommodationId, price, date));
        Assertions.assertEquals(accommodationService.getPrice(accommodationId, date), price);
    }

    //Test that it is possible to change already set prices by calling setPrice() multiple times in a row
    @ParameterizedTest
    @MethodSource("providePrices")
    public void testSettingPricesMultipleTimes(double price) {
        Assertions.assertTrue(accommodationService.setPrice(5L, notDefaultPrice, date));
        Assertions.assertTrue(accommodationService.setPrice(5L, price, date));
        Assertions.assertEquals(accommodationService.getPrice(5L, date), price);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPrices")
    public void testSettingInvalidPrices(double price) {
        Assertions.assertFalse(accommodationService.setPrice(5L, price, date));
        Assertions.assertNotEquals(accommodationService.getPrice(5L, date), price, 0.0);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDates")
    public void testSettingPriceInPast(Date date) {
        Assertions.assertFalse(accommodationService.setPrice(5L, notDefaultPrice, date));
        Assertions.assertNotEquals(accommodationService.getPrice(5L, date), notDefaultPrice, 0.0);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDates")
    public void testMakingUnavailableInPast(Date date) {
        Assertions.assertFalse(accommodationService.setUnavailable(5L, date));
        Assertions.assertTrue(accommodationService.isAvailable(5L, date));
    }

    @ParameterizedTest
    @MethodSource("provideValidDatesAndAccommodations")
    public void testMakingUnavailable(Date date, long accommodationId) {
        Assertions.assertTrue(accommodationService.setUnavailable(accommodationId, date));
        Assertions.assertFalse(accommodationService.isAvailable(accommodationId, date));
    }

    @ParameterizedTest
    @MethodSource("provideValidDatesAndAccommodations")
    public void testMakingAvailable(Date date, long accommodationId) {
        Assertions.assertTrue(accommodationService.setUnavailable(accommodationId, date));
        Assertions.assertTrue(accommodationService.setAvailable(accommodationId, date));
        Assertions.assertFalse(accommodationService.isAvailable(accommodationId, date));
    }

    @ParameterizedTest
    @MethodSource("provideValidAccommodationsAndDeadlines")
    public void testSettingCancellationDeadline(Long accommodationId, int deadline) {
        Accommodation accommodation = accommodationService.findOne(accommodationId);
        Assertions.assertNotNull(accommodation);
        accommodation.setDeadlineForCancellation(deadline);
        accommodationService.update(accommodation);
        Assertions.assertEquals(accommodationService.findOne(accommodationId).getDeadlineForCancellation(), deadline);

    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccommodationsValidDeadlines")
    public void testSettingDeadlineOnInvalidAccommodation(Long accommodationId, int deadline) {
        Accommodation accommodation = accommodationService.findOne(accommodationId);
        Assertions.assertNull(accommodation);
        accommodation = new Accommodation();
        accommodation.setId(accommodationId);
        accommodation.setDeadlineForCancellation(deadline);
        Accommodation finalAccommodation = accommodation;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accommodationService.update(finalAccommodation);
        });
    }

    @ParameterizedTest
    @MethodSource("provideValidAccommodationsInvalidDeadlines")
    public void testSettingInvalidDeadlineOnAccommodation(Long accommodationId, int deadline) {

        Accommodation accommodation = accommodationService.findOne(accommodationId);
        accommodation.setDeadlineForCancellation(deadline);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accommodationService.update(accommodation);
        });
    }

    private static Stream<Arguments> provideValidAccommodationsAndDeadlines() {
        return Stream.of(
                Arguments.of(5L, 15),
                Arguments.of(6L, 25),
                Arguments.of(7L, 39)
        );
    }

    public Stream<Arguments> provideValidAccommodationsInvalidDeadlines() {
        return Stream.of(
                Arguments.of(5L, -1),
                Arguments.of(6L, -5),
                Arguments.of(7L, -500)
        );
    }

    private static Stream<Arguments> provideInvalidAccommodationsValidDeadlines() {
        return Stream.of(
                Arguments.of(9L, 15),
                Arguments.of(19L, 25),
                Arguments.of(20L, 39)
        );
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
