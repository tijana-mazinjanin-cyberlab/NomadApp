package Repositories;

import model.Accommodation;
import model.DateRange;
import model.Reservation;
import model.ReservationDate;
import model.enums.AccommodationType;
import model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findOneById(Long id);


    Collection<Reservation> findAllByAccommodation_Host_id(long userId);


    Collection<Reservation> findAllByGuest_id(long userId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.status = :status " +
            "AND :currentDate BETWEEN r.dateRange.startDate AND r.dateRange.finishDate " +
            "AND r.guest.id = :guestId")
    Collection<Reservation> findAllActiveByGuestId(
            @Param("status") ReservationStatus status,
            @Param("currentDate") Date currentDate,
            @Param("guestId") Long guestId
    );

    @Query("select a from Reservation a " +
            "where a.accommodation.host.id =:hostId " +
            "and (:name = '' OR lower(a.accommodation.name) like lower(CONCAT('%', :name, '%')) )" +
            "and (:status IS NULL OR a.status = :status )")
    List<Reservation> findAllByHost(@Param("hostId")Long hostId, @Param("name")String name,
                                  @Param("status") ReservationStatus status);
    @Query("select a from Reservation a " +
            "where a.guest.id =:guestId " +
            "and (:name = '' OR lower(a.accommodation.name) like lower(CONCAT('%', :name, '%')) )" +
            "and (:status IS NULL OR a.status = :status )")
    List<Reservation> findAllByGuest(@Param("guestId")Long guestId, @Param("name")String name,
                                @Param("status") ReservationStatus status);

    Collection<Reservation> findAllByAccommodation_id(long accommodationId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.status = :status " +
            "AND :currentDate <= r.dateRange.startDate " +
            "AND r.guest.id = :hostId")
    Collection<Reservation> findAllFutureByHost(
            @Param("status") ReservationStatus status,
            @Param("currentDate") Date currentDate,
            @Param("hostId") Long hostId
    );

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.accommodation.id = :id " +
            "and r.status = :status ")
    Collection<Reservation> findForReport(
            @Param("id") Long accommodationId,
            @Param("status") ReservationStatus status
    );
//    AND :currentDate >= r.dateRange.startDate
}
