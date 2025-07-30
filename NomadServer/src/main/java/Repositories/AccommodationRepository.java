package Repositories;

import model.Accommodation;
import model.Amenity;
import model.Host;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

public interface AccommodationRepository extends JpaRepository <Accommodation, Long> {
    Accommodation findOneById(Long id);

    Collection<Accommodation> findAllByVerified(boolean b);

    Collection<Accommodation>findAllByHost_id(long hostId);

    @Query("select a from Accommodation a " +
            "where a.maxGuests >=:peopleNum and a.minGuests<=:peopleNum " +
            "and lower(a.address) like lower(CONCAT('%', :city, '%')) " +
            "and (:type IS NULL OR a.accommodationType = :type )" +
            "and a.status = 0")
    List<Accommodation> findAllBy(@Param("peopleNum")int peopleNum, @Param("city")String city,
                                  @Param("type")AccommodationType accommodationType);

    @Query("select a from Accommodation a " +
            "where (:type IS NULL OR a.accommodationType = :type )" +
            "and a.status = 0")
    List<Accommodation> findAllBy(@Param("type")AccommodationType accommodationType);

    List<Accommodation> findAllByStatus(AccommodationStatus accommodationStatus);
}