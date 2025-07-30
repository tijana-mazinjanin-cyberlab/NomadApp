package DTO;

import model.AppUser;
import model.Comment;

import java.util.List;

public class CommentDTO {
    private String title;
    private String text;
    private AppUser appUser;
    private int rating; // Optional rating from 1 to 5

    public CommentDTO(){}
    public CommentDTO(String title, String text, AppUser appUser, int rating, List<Comment> responses) {
        this.title = title;
        this.text = text;
        this.appUser = appUser;
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public AppUser getUser() {
        return appUser;
    }

    public int getRating() {
        return rating;
    }

}
