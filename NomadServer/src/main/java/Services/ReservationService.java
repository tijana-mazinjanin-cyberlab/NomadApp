package Services;

import DTO.ReportDTO;
import DTO.SearchAccommodationDTO;
import Repositories.IRepository;
import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import Repositories.UserRepository;
import exceptions.NotValidException;
import jakarta.transaction.Transactional;
import model.Accommodation;
import model.DateRange;
import model.Reservation;
import model.ReservationDate;
import model.enums.AccommodationType;
import model.enums.PriceType;
import model.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import util.Helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class ReservationService implements IService<Reservation, Long> {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Override
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findOne(Long id) {
        return reservationRepository.findOneById(id);
    }

    public List<ReportDTO> getReportsFor(DateRange dateRange, Long hostId){
        List<ReportDTO> reports = new ArrayList<>();
        for(Accommodation accommodation: accommodationService.findByHost(hostId)){
            ReportDTO report = new ReportDTO();
            report.setAccommodation_id(accommodation.getId());
            for(Reservation reservation: reservationRepository.findForReport(accommodation.getId(), ReservationStatus.ACCEPTED )){
                if(reservation.getDateRange().isInRange(dateRange)){
                    for (ReservationDate reservationDate: reservationDateRepository.findAllByReservation_id(reservation.getId())){
                        if(accommodation.getPriceType() == PriceType.FOR_GUEST){
                            report.addProfit( reservation.getNumGuests()*reservationDate.getPrice());
                        }else{
                            report.addProfit(reservationDate.getPrice());
                        }
                    }
                    report.increaseResNum();
                }
            }
            reports.add(report);
        }
        return reports;
    }
    public HashMap<Integer, ReportDTO> getReportsFor(int year, Long accommodationId, Long hostId){
        Accommodation accommodation = accommodationService.findOne(accommodationId);
        if (accommodation.getHost().getId() != hostId){
            throw new NotValidException("That is someone else's accommodation");
        }
        HashMap<Integer,ReportDTO> reports = new HashMap<>(12);
        DateRange yearDateRange = Helper.getStartEndDates(year);

        for(Reservation reservation: reservationRepository.findForReport(accommodation.getId(), ReservationStatus.ACCEPTED)){
            if(!yearDateRange.overlaps(reservation.getDateRange())){
                continue;
            }
            if(reservation.isForReservationCount(year)){
                int month = reservation.getMonthForReport();
                if(reports.containsKey(month)){
                    reports.get(month).increaseResNum();
                }else {
                    reports.put(month, new ReportDTO(0.0, 1, 0L));
                }
            }

            for (ReservationDate reservationDate: reservationDateRepository.findAllByReservation_id(reservation.getId())){
                if(Helper.getYear(reservationDate.getDate())!=year){
                    continue;
                }
                int resDateMonth = Helper.getMonth(reservationDate.getDate());
                if(!reports.containsKey(resDateMonth)){
                    reports.put(resDateMonth, new ReportDTO(0.0, 0, 0L));
                }
                if(accommodation.getPriceType() == PriceType.FOR_GUEST){
                    reports.get(resDateMonth).addProfit( reservation.getNumGuests()*reservationDate.getPrice());
                }else{
                    reports.get(resDateMonth).addProfit(reservationDate.getPrice());
                }
            }

        }

        return reports;
    }

    public boolean verify(Reservation reservation) {
        if (reservation == null){ return false; }
        if(reservation.getStatus() == ReservationStatus.CANCELED) { return false; }
        if(reservation.getStatus() == ReservationStatus.REJECTED) { return false; }

        reservation.setStatus(ReservationStatus.ACCEPTED);
        declineOverlaping(reservation);
        this.createReservationDates(reservation);
        reservationRepository.save(reservation);
        return true;
    }
    public void declineOverlaping(Reservation reservation){
        //ArrayList<Reservation> reservations = new ArrayList<>();
        for(Reservation r: reservationRepository.findAllByAccommodation_id(reservation.getAccommodation().getId())){
            if(r.getId() != reservation.getId() && r.getDateRange().overlaps(reservation.getDateRange()) && r.getStatus() == ReservationStatus.PENDING){
                r.setStatus(ReservationStatus.REJECTED);
                //reservations.add(r);
                reservationRepository.save(r);
            }
        }
       // reservationRepository.saveAll(reservations);
    }

    public void decline(Reservation reservation) {
        if(reservation.getStatus() == ReservationStatus.PENDING){
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.save(reservation);
        }
    }
    public void cancel(Long id) {
        Reservation reservation = findOne(id);
        if (reservation == null){
            throw new NullPointerException();
        }
        if(reservation.validForCancel() && reservation.getStatus() == ReservationStatus.ACCEPTED){
           // reservationDateRepository.deleteByReservation_idAndPrice( reservation.getId(),reservation.getAccommodation().getDefaultPrice());
            for(ReservationDate r : reservationDateRepository.findAllByReservation_id(reservation.getId())){
                if(r.getPrice()!=reservation.getAccommodation().getDefaultPrice()){
                    r.setReservation(null);
                    reservationDateRepository.save(r);
                }else{
                    reservationDateRepository.deleteById(r.getId());
                }

            }
            reservation.setStatus(ReservationStatus.CANCELED);
        }else{
            throw new NotValidException("Not valid for cancel");
        }
        reservation.getUser().increaseNumber();
        userRepository.save(reservation.getUser());
        reservationRepository.save(reservation);
    }

    @Override
    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }
    public void createReservationDates(Reservation reservation) {
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getDateRange().getStartDate());
        for(; c.getTime().before(reservation.getDateRange().getFinishDate()); c.add(Calendar.DATE, 1)){
            this.createReservationDate(new ReservationDate(reservation.getAccommodation(), reservation, reservation.getAccommodation().getDefaultPrice(), c.getTime()));
        }
    }
    public Collection<Reservation> getFilteredHost(Long hostId, String name, Date startDate, Date endDate, ReservationStatus reservationStatus) {
        Collection<Reservation> filtered = new ArrayList<>();
        for (Reservation r: reservationRepository.findAllByHost(hostId, name, reservationStatus)) {
           if(startDate == null){
               filtered.add(r);
           }else if(r.getDateRange().isInRange(new DateRange(startDate, endDate))){
                filtered.add(r);
            }
        }
        return filtered;
    }
    public Collection<Reservation> getFilteredGuest(Long guestId, String name, Date startDate, Date endDate, ReservationStatus reservationStatus) {
        Collection<Reservation> filtered = new ArrayList<>();
        for (Reservation r: reservationRepository.findAllByGuest(guestId, name, reservationStatus)) {
            if(startDate == null){
                filtered.add(r);
            }else if(r.getDateRange().isInRange(new DateRange(startDate, endDate))){
                filtered.add(r);
            }
        }
        return filtered;
    }
    public Collection<Reservation> findReservationsForHost(long userId){
        return reservationRepository.findAllByAccommodation_Host_id(userId);
    }
    public Collection<Reservation> findReservationsForGuest(long userId){
        return reservationRepository.findAllByGuest_id(userId);
    }
    public Collection<Reservation> findActiveReservationsForHost(Long hostId) {
        return this.reservationRepository.findAllFutureByHost(ReservationStatus.ACCEPTED, new Date(), hostId);
    }
    
    public Collection<Reservation> findActiveReservationsForGuest(long guestId) {
        return this.reservationRepository.findAllActiveByGuestId(ReservationStatus.ACCEPTED, new Date(), guestId);
    }
    private void createReservationDate(ReservationDate reservationDate) {
        ReservationDate reservationDateToUpdate = reservationDateRepository.findOneByAccommodation_IdAndDate(reservationDate.getAccommodation().getId(), reservationDate.getDate());
        if(reservationDateToUpdate == null){
            reservationDateRepository.save(reservationDate);
            return;
        }
        reservationDateToUpdate.setReservation(reservationDate.getReservation());
        reservationDateRepository.save(reservationDateToUpdate);
    }

    public boolean reserveAutomatically(Reservation reservation){
        if(!this.isAvailable(reservation)){
            return false;
        }
        reservation.setStatus(ReservationStatus.ACCEPTED);
        this.create(reservation);
        this.createReservationDates(reservation);
        return true;
    }
    public boolean reserveManually(Reservation reservation){
        if(!this.isAvailable(reservation)){
            return false;
        }
        reservation.setStatus(ReservationStatus.PENDING);
        this.create(reservation);
        return true;
    }
    public boolean isAvailable(Reservation reservation){
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getDateRange().getStartDate());
        for(; c.getTime().before(reservation.getDateRange().getFinishDate()); c.add(Calendar.DATE, 1)){
            System.out.println(c.getTime());
            System.out.println(c.getTime().getTime());
            if (!accommodationService.isAvailable(reservation.getAccommodation().getId(), c.getTime()) ){
                return false;
            }

        }

        return true;
    }
    @Override
    public void update(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reservationDateRepository.deleteByReservation_id(id);

        reservationRepository.deleteById(id);
    }
    public boolean validateReservation(Reservation reservation){
        if(reservation.getAccommodation() == null){
            return false;
        }
        if(reservation.getNumGuests() < reservation.getAccommodation().getMinGuests()){
            return false;
        }
        if(reservation.getNumGuests() > reservation.getAccommodation().getMaxGuests()){
            return false;
        }
        if(reservation.getUser() == null){
            return false;
        }
        return true;
    }

}
