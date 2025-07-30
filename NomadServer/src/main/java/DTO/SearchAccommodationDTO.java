package DTO;

import model.*;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import model.enums.ConfirmationType;
import model.enums.PriceType;

import java.util.ArrayList;
import java.util.List;

public class SearchAccommodationDTO extends AccommodationDTO{
    //List<AccommodationRating> ratings;
    Double totalPrice;
    Double pricePerNight;
    double averageRating;

    public SearchAccommodationDTO(){}

    public SearchAccommodationDTO(long id, int minGuests, int maxGuests, String name, String description, String address, List<Amenity> amenities, List<String> images, List<Comment> comments, List<Rating> ratings, AccommodationStatus status, ConfirmationType confirmationType, AccommodationType accommodationType, PriceType priceType, double defaultPrice, int deadlineForCancellation, boolean verified, Double totalPrice, Double pricePerNight, double averageRating) {
        super(id, minGuests, maxGuests, name, description, address, amenities, images, comments, ratings, status, confirmationType, accommodationType, priceType, defaultPrice, deadlineForCancellation, verified);
        this.totalPrice = totalPrice;
        this.pricePerNight = pricePerNight;
        this.averageRating = averageRating;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int nights) {
        this.pricePerNight = totalPrice/nights;
    }

    public double getAverageRating() {
        return averageRating;
    }

//    public List<AccommodationRating> getRatings() {
//        return ratings;
//    }
//
//    public void setRatings(List<AccommodationRating> ratings) {
//        this.ratings = ratings;
//    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

//    public void setAverageRating(double averageRating) {
//        this.averageRating = averageRating;
//    }

    public void setAverageRating(List<AccommodationRating> ratings) {
        for(Rating rating: ratings){
            this.averageRating += rating.getRating();
        }
        if(!ratings.isEmpty()){
            this.averageRating = this.averageRating/ratings.size();
        }else{
            this.averageRating = 0;
        }

    }
}
