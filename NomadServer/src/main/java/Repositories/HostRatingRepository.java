package Repositories;

import model.AccommodationRating;
import model.HostRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface HostRatingRepository extends JpaRepository<HostRating, Long> {
    HostRating findOneById(Long id);

    Collection<HostRating> findAllByHost_Id(Long id);

    HostRating findOneByAppUser_IdAndHost_Id(Long userId, Long accommodationId);

    Collection<Object> findAllByAppUser_IdAndHost_Id(Long userId, Long accommodationId);
}
