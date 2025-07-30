package Services;

import Repositories.*;
import model.Accommodation;
import model.AccommodationRating;
import model.Guest;
import model.ReservationDate;
import model.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class AccommodationRatingService implements IService<AccommodationRating, Long> {

    @Autowired
    private AccommodationRatingRepository accommodationRatingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Override
    public Collection<AccommodationRating> findAll() {
        return accommodationRatingRepository.findAll();
    }

    @Override
    public void create(AccommodationRating object) {
        object.setUser(userRepository.findOneById(object.getUserId()));
        object.setAccommodation(accommodationRepository.findOneById(object.getAccommodationId()));
        accommodationRatingRepository.save(object);
    }

    @Override
    public AccommodationRating findOne(Long id) {
        return accommodationRatingRepository.findOneById(id);
    }

    @Override
    public void update(AccommodationRating object) {
        accommodationRatingRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        accommodationRatingRepository.deleteById(id);
    }

    public Collection<AccommodationRating> findRatingsForAccommodation(Long id) {
        return accommodationRatingRepository.findAllByAccommodation_Id(id);
    }
    public boolean canRate(Long userId, Long accommodationId){
        LocalDate cutOffDate = LocalDate.now().minusDays(7);
        Collection<ReservationDate> dates = reservationDateRepository.findDatesForAllowingComment(accommodationId, userId,
                Date.from(cutOffDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); //This mess gets the Date() 7 days before now
        if(dates.isEmpty()){
            return false;
        }
        for (ReservationDate date: dates){
            if (date.getReservation().getStatus() == ReservationStatus.ACCEPTED){
                return accommodationRatingRepository.findAllByAppUser_IdAndAccommodation_Id(userId, accommodationId).isEmpty();
            }
        }
        return false;
    }
    public boolean hasComment(Long userId){
        return !accommodationRatingRepository.findAllByAppUser_Id(userId).isEmpty();
    }

    public Boolean hasComment(Long userId, Long accommodationId) {
        return !accommodationRatingRepository.findAllByAppUser_IdAndAccommodation_Id(userId, accommodationId).isEmpty();
    }

    public Long findOneForUserAndAccommodation(Long userId, Long accommodationId) {
        AccommodationRating rating;
        try{
             rating = accommodationRatingRepository.findOneByAppUser_IdAndAccommodation_Id(userId, accommodationId);

        }catch (Exception e){    //This ensures backwards compatibility with versions of the database
                                                                    //from when we allowed multiple comments by the same user
            rating = (AccommodationRating) accommodationRatingRepository.findAllByAppUser_IdAndAccommodation_Id(userId, accommodationId).toArray()[0];
        }
        if(rating == null){
            return -1L;
        }
        return rating.getId();
    }
}
