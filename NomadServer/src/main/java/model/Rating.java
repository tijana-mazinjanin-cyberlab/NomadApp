package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ratings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int rating; // rating from 1 to 5
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;
    @OneToMany(mappedBy = "reportedRating", cascade = CascadeType.REMOVE)
    private List<CommentReport> ratingReports;

    public Rating() {}
    public Rating(int rating, AppUser appUser, String text) {
        this.rating = rating;
        this.appUser = appUser;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
