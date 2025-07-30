package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ReservationDate {
    @Id
    @GeneratedValue
    Long id;
    @ManyToOne (fetch = FetchType.LAZY)
    Accommodation accommodation;
    @ManyToOne (cascade=CascadeType.ALL)
    Reservation reservation;

    double price;
    @JsonFormat(pattern="yyyy-MM-dd")
    Date date;

    public ReservationDate(){}

    public ReservationDate(Accommodation accommodation, Reservation reservation, double price, Date date) {
        this.accommodation = accommodation;
        this.reservation = reservation;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
