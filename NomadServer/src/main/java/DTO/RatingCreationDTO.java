package DTO;

public class RatingCreationDTO {
    private Long userId;
    private Long ratedId;
    private String text;
    private int rating;

    public RatingCreationDTO() {
    }

    public RatingCreationDTO(Long userId, Long ratedId, String text, int rating) {
        this.userId = userId;
        this.ratedId = ratedId;
        this.text = text;
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRatedId() {
        return ratedId;
    }

    public void setRatedId(Long ratedId) {
        this.ratedId = ratedId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
