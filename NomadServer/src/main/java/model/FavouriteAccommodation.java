package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "favourites")
public class FavouriteAccommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne (fetch = FetchType.LAZY, cascade = {})
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Guest guest;

    @ManyToOne (fetch = FetchType.LAZY, cascade = {})
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Accommodation accommodation;

    public FavouriteAccommodation() {};

    public FavouriteAccommodation(Long id, Guest guest, Accommodation accommodation) {
        this.id = id;
        this.guest = guest;
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public String toString() {
        return "FavouriteAccommodation{" +
                "id=" + id +
                ", guest=" + guest +
                ", accommodation=" + accommodation +
                '}';
    }


}
