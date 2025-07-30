package Services;

import Repositories.FavouriteAccommodationRepository;
import model.FavouriteAccommodation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan("Repositories")
public class FavouriteAccommodationService implements IService<FavouriteAccommodation, Long>{

    @Autowired
    FavouriteAccommodationRepository favouriteRepository;
    @Override
    public Collection<FavouriteAccommodation> findAll() { return favouriteRepository.findAll(); }

    @Override
    public void create(FavouriteAccommodation object) { favouriteRepository.save(object); }

    @Override
    public FavouriteAccommodation findOne(Long id) { return favouriteRepository.findOneById(id); }

    @Override
    public void update(FavouriteAccommodation object) { favouriteRepository.save(object); }

    @Override
    public void delete(Long id) { favouriteRepository.deleteById(id); }

    public Collection<FavouriteAccommodation> findAllForGuest(Long guestId) {
        return this.favouriteRepository.findAllByGuest_id(guestId);
    }

    public FavouriteAccommodation findForAccommodationAndGuest(Long accommodationId, Long guestId) {
        return this.favouriteRepository.findOneByAccommodation_idAndGuest_id(accommodationId, guestId);
    }
}
