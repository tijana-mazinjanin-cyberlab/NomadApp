package model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class AccommodationRating extends Rating{
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;
    @Transient
    private Long userId;
    @Transient
    private Long accommodationId;

    public AccommodationRating() {}

    public AccommodationRating(AppUser appUser, Accommodation accommodation, int rating, String text) {
        super(rating, appUser, text);
        this.accommodation = accommodation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    @Override
    public String toString() {
        return "AccommodationRating{" +
                "user=" + getUser() +
                ", accommodation=" + accommodation +
                ", rating=" + getRating() +
                '}';
    }
}
