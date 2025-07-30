package Repositories;

import model.FavouriteAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface FavouriteAccommodationRepository extends JpaRepository<FavouriteAccommodation, Long> {

    FavouriteAccommodation findOneById(Long id);
    Collection<FavouriteAccommodation> findAllByGuest_id(Long guestId);
    public FavouriteAccommodation findOneByAccommodation_idAndGuest_id(Long accommodationId, Long guestId);
}
