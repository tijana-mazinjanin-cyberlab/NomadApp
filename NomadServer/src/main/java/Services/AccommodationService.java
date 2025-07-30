package Services;

import DTO.AccommodationDTO;
import DTO.SearchAccommodationDTO;
import Repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import model.*;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import model.enums.PriceType;
import model.enums.ReservationStatus;
import net.bytebuddy.implementation.bytecode.Throw;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ComponentScan("Repositories")
@EnableJpaRepositories("Repositories")
public class AccommodationService implements IService<Accommodation, Long> {

    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Collection<Accommodation> findAll() {
        return  accommodationRepository.findAll();
    }

    @Override
    public Accommodation findOne(Long id) {
        return accommodationRepository.findOneById(id);
    }

    @Override
    public void create(Accommodation accommodation) {
        accommodationRepository.save(accommodation);
    }
    public Accommodation createAccommodation(Accommodation accommodation) {
        return accommodationRepository.save(accommodation);
    }

    @Override
    public void update(Accommodation accommodation) {
        if(accommodationRepository.findOneById(accommodation.getId()) == null){
            throw (new IllegalArgumentException());
        }
        if(!validateAccommodation(accommodation)){
            throw (new IllegalArgumentException());
        }
        accommodationRepository.save(accommodation);
    }
    public int getDeadlineForCancellation(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        if(accommodation == null){
            throw (new IllegalArgumentException());
        }
        return accommodation.getDeadlineForCancellation();

    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }

    public Collection<Accommodation> findByHost(Long host) { return this.accommodationRepository.findAllByHost_id(host); }

    public Collection<Accommodation> findApprovedAccommodations () {return this.accommodationRepository.findAllByStatus(AccommodationStatus.APPROVED);}

    public void deleteAllForHost(Long hostId) {
        for(Accommodation ac: this.accommodationRepository.findAllByHost_id(hostId)) {
            this.accommodationRepository.delete(ac);
        }
    }

    public Collection<SearchAccommodationDTO> getSearchedAndFiltered(String city, DateRange dateRange, int peopleNum, Double minimumPrice,
                                                     Double maximumPrice, List<Long> amenity, AccommodationType type) {
        Collection<SearchAccommodationDTO> filtered = new ArrayList<>();
        for (Accommodation a: this.getFilteredAccommodations(peopleNum, city, type, amenity)) {
            SearchAccommodationDTO accommodationDTO = this.checkAvailability(a, dateRange, minimumPrice, maximumPrice, peopleNum);
            if(accommodationDTO != null){
                filtered.add(accommodationDTO);
            }
        }
        return filtered;
    }
    public Collection<Accommodation> getFiltered(Double minimumPrice, Double maximumPrice, List<Long> amenity, AccommodationType type) {
        Collection<Accommodation> filtered = new ArrayList<>();

        for (Accommodation a: this.getFilteredAccommodations(type, amenity)) {
            if(minimumPrice != null && maximumPrice != null){
                if(isPriceInRange(a,minimumPrice, maximumPrice, 1)){
                    filtered.add(a);
                }
            }else{
                filtered.add(a);
            }
        }
        return filtered;
    }

    public boolean isAvailable(long accommodationId, Date date){
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if (reservationDate == null){
            return true;
        }else{
            return reservationDate.getReservation() == null;
        }
    }
    public List<Accommodation> getFilteredAccommodations(int peopleNum, String city, AccommodationType accommodationType, List<Long> amenities){
        List<Accommodation> accommodations =  accommodationRepository.findAllBy(peopleNum, city, accommodationType);
        List<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: accommodations) {
            if(hasAllAmenities(a, amenities)){
                filtered.add(a);
            }
        }
        return filtered;
    }
    public List<Accommodation> getFilteredAccommodations(AccommodationType accommodationType, List<Long> amenities){
        List<Accommodation> accommodations =  accommodationRepository.findAllBy(accommodationType);
        List<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: accommodations) {
            if(hasAllAmenities(a, amenities)){
                filtered.add(a);
            }
        }
        return filtered;
    }

    public boolean hasAllAmenities(Accommodation accommodation, List<Long> amenities){
        int found = 0;
        if(amenities == null){
            return true;
        }
        for (Long id: amenities) {
            for (Amenity amenity: accommodation.getAmenities()) {
                if (id == amenity.getId()){
                    found++;
                    break;
                }
            }
        }
        if(found == amenities.size()){
            return true;
        }
        return false;
    }

    public Double checkAvailabilityAndGetPrice(Accommodation accommodation, Date date, Double priceMin, Double priceMax, int peopleNum){
        ReservationDate reservationDate = reservationDateRepository.findBy(accommodation.getId(), date, priceMin, priceMax, peopleNum);

        if (reservationDate == null){
            //goes here if reservationDate doesn't exist for date
            if(priceMin != null && priceMax != null){
                if(isPriceInRange(accommodation, priceMin,priceMax, peopleNum)){
                    return accommodation.getDefaultPrice();
                }else{
                    return null;
                }
            }
            return accommodation.getDefaultPrice();
        }
        if(reservationDate.getReservation() == null) {
            return reservationDate.getPrice();
        }
        return null;
    }
    public boolean isPriceInRange(Accommodation accommodation, Double priceMin, Double priceMax, int peopleNum){
        Double accomodationPrice = accommodation.getDefaultPrice();
        if(accommodation.getPriceType() == PriceType.FOR_ACCOMMODATION){
            accomodationPrice = accomodationPrice/peopleNum;
        }
        if(accomodationPrice<=priceMax && accomodationPrice>=priceMin){
            return true;
        }else{
            return false;
        }
    }

    public SearchAccommodationDTO checkAvailability(Accommodation accommodation, DateRange dateRange, Double priceMin, Double priceMax, int peopleNum){
        Calendar c = Calendar.getInstance();
        c.setTime(dateRange.getStartDate());
        Double totalPrice = 0.0;
        int nights = 0;
        for(; c.getTime().before(dateRange.getFinishDate()); c.add(Calendar.DATE, 1)){
            Double price = this.checkAvailabilityAndGetPrice(accommodation, c.getTime(), priceMin, priceMax, peopleNum);
            if (price == null){
                return null;
            }
            nights++;
            totalPrice += price;
        }
        if(accommodation.getPriceType() == PriceType.FOR_GUEST){
            totalPrice *= peopleNum;
        }
        return convertToSearchDto(accommodation, nights, totalPrice);
    }
    private SearchAccommodationDTO convertToSearchDto(Accommodation accommodation, int nights, Double totalPrice) {
        SearchAccommodationDTO accommodationDTO = modelMapper.map(accommodation, SearchAccommodationDTO.class);
        accommodationDTO.setAverageRating(accommodation.getRatings());
        accommodationDTO.setTotalPrice(totalPrice);
        accommodationDTO.setPricePerNight(nights);
        return accommodationDTO;
    }
//    public List<LocalDate> getTakenDates(long accommodationId, Date start, Date end){
//        LocalDate startLocal = LocalDate.from(start.toInstant());
//        LocalDate endLocal = LocalDate.from(end.toInstant());
//        return startLocal.datesUntil(endLocal).filter(date -> {
//            return this.isAvailable(accommodationId, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }).collect(Collectors.toList());
//    }
    public List<Date> getTakenDates(long accommodationId){
        List<ReservationDate> reservationDates = reservationDateRepository.findAllByAccommodation_id(accommodationId);
        return reservationDates.stream().filter(reservationDate -> {
            return reservationDate.getReservation()!=null;
        }).map(ReservationDate::getDate).collect(Collectors.toList());
    }

    public List<Amenity> getAllAmenitiesForAccommodation(long accommodationId) {
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        return accommodation.getAmenities();
    }
    public Collection<Accommodation> getUnverifiedAccommodations() {return accommodationRepository.findAllByVerified(false);}

    public void addAmenityToAccommodation (long accommodationId, Amenity newAmenity) {
        Amenity savedAmenity = amenityRepository.save(newAmenity);
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        List<Amenity> amenities = accommodation.getAmenities();
        amenities.add(savedAmenity);
        accommodation.setAmenities(amenities);
        accommodationRepository.save(accommodation);
    }
    public Double getPrice(long accommodationId, Date date) {
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        if(accommodation == null){
            throw new IllegalArgumentException("Non-existent accommodation");
        }
        if (reservationDate == null){
            return accommodation.getDefaultPrice();
        }else{
            if(reservationDate.getPrice() != 0){
                return reservationDate.getPrice();
            }
            return accommodation.getDefaultPrice();
        }
    }

    public boolean setPrice(long accommodationId, double price, Date date) {
        if(price < 0){
            //Price is less than 0 (assuming we allow "free" dates
            return false;
        }
        if(accommodationRepository.findOneById(accommodationId) == null){
            //Accommodation doesn't exist
            return false;
        }
        if(date.before(new Date())){
            //Date is before now
            return false;
        }
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if(reservationDate != null) {
            if(reservationDate.getReservation() != null){
                if(reservationDate.getReservation().getUser() != null){
                    //cannot update price if there is available reservation
                    return false;
                }
            }

            reservationDate.setPrice(price);
            this.reservationDateRepository.save(reservationDate);
            return true;
        }

        reservationDate = new ReservationDate(this.accommodationRepository.findOneById(accommodationId), null, price, date);
        this.reservationDateRepository.save(reservationDate);
        return true;
    }

    public boolean setPriceForDateRange(long accommodationId, double price, DateRange dateRange) {
        Date startDate = dateRange.getStartDate();
        Date endDate = dateRange.getFinishDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            Date currentDate = calendar.getTime();
            if(!setPrice(accommodationId, price, currentDate)){
                return false;
            }
            calendar.add(Calendar.DATE, 1);
        }
        return true;
    }

    public boolean setUnavailable(long accommodationId, Date date) {
        if(date.before(new Date())){
            //Date is before now
            return false;
        }
        if(accommodationRepository.findOneById(accommodationId) == null){
            //Accommodation doesn't exist
            return false;
        }
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if(reservationDate != null) {
            if(reservationDate.getReservation() == null) {
                Reservation reservation = new Reservation();
                reservation.setUser(null);
                reservationDate.setReservation(reservation);

                this.reservationDateRepository.save(reservationDate);
                return true;
            }
        }
        Accommodation ac = this.accommodationRepository.findOneById(accommodationId);
        reservationDate = new ReservationDate(ac,
                new Reservation(null, ac, null, 0, ReservationStatus.REJECTED),
                0, date);
        this.reservationDateRepository.save(reservationDate);
        return true;
    }

    public void setUnavailableForDateRange(long accommodationId, DateRange dateRange) {
        Date startDate = dateRange.getStartDate();
        Date endDate = dateRange.getFinishDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            Date currentDate = calendar.getTime();
            if(!setUnavailable(accommodationId, currentDate)){
                throw new EntityNotFoundException("Accommodation doesn't exist");
            }
            calendar.add(Calendar.DATE, 1);
        }
    }

    public boolean setAvailable(long accommodationId, Date date) {
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if(reservationDate.getReservation().getUser().getUsername() == null){
            reservationDate.getReservation().setUser(null);
        }
        if(date.before(new Date())){
            //Date is before now
            return false;
        }
        if(accommodationRepository.findOneById(accommodationId) == null){
            //Accommodation doesn't exist
            return false;
        }
        if(reservationDate == null) {
            System.out.println("Already available");
            //already available
            return false;
        }

        if(reservationDate.getReservation() != null){
            //there is active reservation for this date
            if(reservationDate.getReservation().getUser() != null) {
                System.out.println("There is an active reservation for date: " + date.toString());
                return false;
            }

            System.out.println("Host previously set up accommodation to unavailable");
            this.reservationDateRepository.delete(reservationDate);
            return true;
        }

        //only price is stored in this reservationDate
        System.out.println("Only price is stored in this reservationDate");
        this.reservationDateRepository.delete(reservationDate);
        return true;
    }

    public void setAvailableForDateRange(long accommodationId, DateRange dateRange) {
        Date startDate = dateRange.getStartDate();
        Date endDate = dateRange.getFinishDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            Date currentDate = calendar.getTime();
            setAvailable(accommodationId, currentDate);
            calendar.add(Calendar.DATE, 1);
        }
    }

    public boolean validateAccommodation(Accommodation accommodation){
        if (accommodation.getAmenities() == null){
            accommodation.setAmenities(new ArrayList<Amenity>());
        }
        if (accommodation.getAddress() == null){
            return false;
        }
        if (accommodation.getAddress().length() < 2){
            return false;
        }
        if (accommodation.getHost() == null){
            return false;
        }
        if (accommodation.getAccommodationType() == null){
            return false;
        }
        if (accommodation.getImages() == null){
            accommodation.setImages(new ArrayList<String>());
        }
        if(accommodation.getDeadlineForCancellation() < 0){
            return false;
        }
        if(accommodation.getDescription() == null){
            return false;
        }
        if(accommodation.getDescription().length() < 3){
            return false;
        }
        if(accommodation.getMinGuests() > accommodation.getMaxGuests() || accommodation.getMinGuests() <= 0){
            return false;
        }
        if(accommodation.getDefaultPrice() < 1){
            return false;
        }
        return true;
    }
}
