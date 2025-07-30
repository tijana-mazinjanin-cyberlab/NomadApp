package model;

import jakarta.persistence.*;
import model.enums.ReservationStatus;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;
import util.Helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne (fetch = FetchType.LAZY)
    private Guest guest;
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @Embedded
    private DateRange dateRange;
    private int numGuests;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // Enum to represent reservation status: pending, accepted, rejected

    // Constructor
    public Reservation(Guest guest, Accommodation accommodation, DateRange dateRange, int numGuests, ReservationStatus status) {
        this.guest = guest;
        this.accommodation = accommodation;
        this.dateRange = dateRange;
        this.numGuests = numGuests;
        this.status = status;
    }

    public Reservation() {}

    public int getMonthForReport(){
        Calendar c = Calendar.getInstance();
        c.setTime(dateRange.getStartDate());
        int[] months = new int[12];
        Arrays.fill(months, 0);
        for(; c.getTime().before(dateRange.getFinishDate()); c.add(Calendar.DATE, 1)){
            LocalDate localDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            months[month-1] +=1;
        }
        int max = 0;
        int month = 0;
        for(int i = 0; i < 12; i++){
            if(months[i] > max){
                month = i;
                max = months[i];
            }
        }
        return ++month;
    }
    public boolean isForReservationCount(int year){
        if(isBetweenYears() && isEndOfYear(year)){
            return getMonthForReport() == 12;
        }else if(isBetweenYears() && !isEndOfYear(year) ){
            return getMonthForReport() == 1;
        }else return !isBetweenYears();
    }

    public boolean isEndOfYear(int year){
        return Helper.getYear(this.dateRange.getStartDate()) == year;
    }
    public boolean isBetweenYears(){
        int fromMonth = Helper.getYear(this.dateRange.getStartDate());
        int toMonth = Helper.getYear(this.dateRange.getFinishDate());
        return fromMonth != toMonth;
    }

    public boolean validForCancel(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRange.getStartDate());
        Date currentDate = Calendar.getInstance().getTime();
        //if reservation has started already it is impossible to cancel it
        if(calendar.getTime().before(currentDate)){
            return false;
        }

        //if today's date is before start of reservation
        long differenceInMillis = Math.abs(currentDate.getTime() - dateRange.getStartDate().getTime());
        // Convert milliseconds to days
        long daysDifference = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);
        if(daysDifference >= accommodation.getDeadlineForCancellation()){
            return true;
        }

        return false;
    }

    // Getters and setters for each attribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public Guest getUser() {
        return guest;
    }



    public void setUser(Guest appUser) {
        this.guest = appUser;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void copyValues(Reservation reservation) {
        this.id = reservation.id;
        this.guest = reservation.guest;
        this.accommodation = reservation.accommodation;
        this.dateRange = reservation.dateRange;
        this.numGuests = reservation.numGuests;
        this.status = reservation.status;
    }
}
