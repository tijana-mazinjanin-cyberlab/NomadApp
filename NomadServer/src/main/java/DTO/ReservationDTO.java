package DTO;

import model.DateRange;
import model.enums.ReservationStatus;

import java.util.Date;

public class ReservationDTO {
    private long id;
    private long user;
    private long accommodation;
    private Date startDate;
    private Date finishDate;
    private int numGuests;
    private ReservationStatus status;

    // Constructor
    public ReservationDTO(
            long id,
            long user,
            long accommodation,
            Date startDate,
            Date finishDate,
            int numGuests,
            ReservationStatus status
    ) {
        this.user = user;
        this.accommodation = accommodation;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.numGuests = numGuests;
        this.status = status;
        this.id = id;
    }

    public ReservationDTO () {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(long accommodation) {
        this.accommodation = accommodation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
}
