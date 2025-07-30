package DTO;

import model.Accommodation;
import model.FavouriteAccommodation;
import model.Guest;

public class FavouriteAccommodationDTO {
    Long id;

    private Guest guest;

    private Accommodation accommodation;

    public FavouriteAccommodationDTO(){}

    public FavouriteAccommodationDTO(Long id, Guest guest, Accommodation accommodation) {
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
}
