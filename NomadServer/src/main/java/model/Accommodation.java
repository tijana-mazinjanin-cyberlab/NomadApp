package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import model.enums.ConfirmationType;
import model.enums.PriceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "accommodations")
public class Accommodation implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne (fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Host host;
    private int minGuests;
    private int maxGuests;
    private String name;
    private String description;
    private String address;
    @ManyToMany (fetch = FetchType.EAGER)
    private List<Amenity> amenities;
    @ElementCollection
    private List<String> images;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "accommodation")
    private List<AccommodationComment> comments;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "accommodation")
    private List<AccommodationRating> ratings;
    private AccommodationStatus status;
    @OneToMany (cascade =  CascadeType.ALL)
    private List<ReservationDate> reeservationDates;
    private ConfirmationType confirmationType;
    private AccommodationType accommodationType;
    private PriceType priceType;
    private double defaultPrice;
    private int deadlineForCancellation;
    private boolean verified;
    public Accommodation(){}

    // Constructor
    public Accommodation(long id, Host host, int minGuests, int maxGuests, String name, String description,
                         String address, List<Amenity> amenities, List<String> images, List<AccommodationComment> comments,
                         List<AccommodationRating> ratings, AccommodationStatus status, ConfirmationType confirmationType,
                         AccommodationType accommodationType, PriceType priceType, double defaultPrice, int deadlineForCancellation, boolean verified) {
        this.id = id;
        this.host = host;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.name = name;
        this.description = description;
        this.address = address;
        this.amenities = amenities;
        this.images = images;
        this.comments = comments;
        this.ratings = ratings;
        this.status = status;
        this.confirmationType = confirmationType;
        this.accommodationType = accommodationType;
        this.priceType = priceType;
        this.defaultPrice = defaultPrice;
        this.deadlineForCancellation = deadlineForCancellation;
        this.verified = verified;
    }

    public List<AccommodationRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<AccommodationRating> ratings) {
        this.ratings = ratings;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(ConfirmationType confirmationType) {
        this.confirmationType = confirmationType;
    }

    public int getDeadlineForCancellation() {
        return deadlineForCancellation;
    }

    public void setDeadlineForCancellation(int deadlineForCancellation) {
        this.deadlineForCancellation = deadlineForCancellation;
    }
    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addComment(AccommodationComment comment){
        this.comments.add(comment);
    }

    public List<AccommodationComment> getComments() {
        return comments;
    }

    public void setComments(List<AccommodationComment> comments) {
        this.comments = comments;
    }


    public void addImage(String image){
        this.images.add(image);
    }


    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", host=" + host +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", amenities=" + amenities +
                ", images=" + images +
                ", comments=" + comments +
                ", status=" + status +
                ", confirmationType=" + confirmationType +
                ", deadlineForCancellation=" + deadlineForCancellation +
                ", accommodationType=" + accommodationType +
                '}';
    }

    public void copyValues(Accommodation accommodation) {
        this.minGuests = accommodation.minGuests;
        this.maxGuests = accommodation.maxGuests;
        this.name = accommodation.name;
        this.description = accommodation.description;
        this.address = accommodation.address;
        this.amenities = accommodation.amenities;
        this.images = accommodation.images;
        this.status = accommodation.status;
        this.deadlineForCancellation = accommodation.deadlineForCancellation;
        this.confirmationType = accommodation.confirmationType;
        this.accommodationType = accommodation.accommodationType;
        this.priceType = accommodation.priceType;
        this.defaultPrice = accommodation.defaultPrice;
    }
}