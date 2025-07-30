package com.example.demo;

import Repositories.ReservationRepository;
import Services.ReservationService;
import model.*;
import model.enums.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationServiceTest {

    @Autowired
    @InjectMocks
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    static Stream<Reservation> invalidReservationsForVerifying() {
        return Stream.of(
                null,
                new Reservation(null, null, null, 0, ReservationStatus.REJECTED),
                new Reservation(null, null, null, 0, ReservationStatus.CANCELED)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidReservationsForVerifying")
    public void testVerifyReservationReturnFalse(Reservation reservation) {
        boolean result = reservationService.verify(reservation);
        assertFalse(result);
    }

    @Test
    public void testVerifyCancelsOverlappingReservations() {
        Accommodation accommodation = new Accommodation(1L, new Host(), 1, 2, " ", " "," ", null, null, null,
                null, AccommodationStatus.APPROVED, ConfirmationType.MANUAL, AccommodationType.HOUSE, PriceType.FOR_GUEST, 100, 12, true);

        Reservation reservation = new Reservation(new Guest(), accommodation, new DateRange("2024-02-03", "2024-02-10"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation1 = new Reservation(new Guest(), accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation2 = new Reservation(new Guest(), accommodation, new DateRange("2024-01-29", "2024-02-03"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation3 = new Reservation(new Guest(), accommodation, new DateRange("2024-01-29", "2024-02-11"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation4 = new Reservation(new Guest(), accommodation, new DateRange("2024-02-6", "2024-02-13"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation5 = new Reservation(new Guest(), accommodation, new DateRange("2024-02-10", "2024-02-13"), 1, ReservationStatus.PENDING);

        when(reservationRepository.findAllByAccommodation_id(anyLong())).thenReturn(
                List.of(overlappingReservation1, overlappingReservation2, overlappingReservation3, overlappingReservation4, overlappingReservation5)
        );

        when(reservationRepository.save(overlappingReservation1)).thenReturn(overlappingReservation1);
        when(reservationRepository.save(overlappingReservation2)).thenReturn(overlappingReservation2);
        when(reservationRepository.save(overlappingReservation3)).thenReturn(overlappingReservation3);
        when(reservationRepository.save(overlappingReservation4)).thenReturn(overlappingReservation4);
        when(reservationRepository.save(overlappingReservation5)).thenReturn(overlappingReservation5);

        boolean result = reservationService.verify(reservation);
        assertTrue(result);

        assertEquals(ReservationStatus.REJECTED, overlappingReservation1.getStatus());
        assertEquals(ReservationStatus.REJECTED, overlappingReservation2.getStatus());

        verify(reservationRepository, times(5)).save(any(Reservation.class));
    }

    public void testAcceptReservationWithoutCancellationOfOther() {

    }

    public void testCancelReservation() {

    }

    public void testReserveManuallyValid() {

    }

    public void testReserveAutomaticallyValid() {

    }

    public void testReserveManuallyWhenAccommodationNotAvailable() {

    }

    public void testReserveAutomaticallyWhenAccommodationNotAvailable() {

    }

    public void testAccommodationAvailabilityForReservationValid() {

    }

    public void testAccommodationAvailabilityForReservationInvalid() {

    }
}
