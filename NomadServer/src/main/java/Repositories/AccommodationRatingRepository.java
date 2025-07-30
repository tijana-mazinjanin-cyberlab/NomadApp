package Repositories;

import model.AccommodationRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AccommodationRatingRepository extends JpaRepository<AccommodationRating, Long> {
    AccommodationRating findOneById(Long id);



    Collection<AccommodationRating> findAllByAccommodation_Id(Long id);

    Collection<AccommodationRating> findAllByAppUser_Id(Long userId);

    Collection<Object> findAllByAppUser_IdAndAccommodation_Id(Long userId, Long accommodationId);

    AccommodationRating findOneByAppUser_IdAndAccommodation_Id(Long userId, Long accommodationId);
}
