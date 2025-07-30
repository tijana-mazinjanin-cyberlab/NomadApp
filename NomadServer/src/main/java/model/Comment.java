package model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.io.Serializable;

@Entity
@Table (name = "comments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne
    private AppUser appUser;

    public Comment(){}

    public Comment(String text, AppUser appUser) {
        this.text = text;
        this.appUser = appUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + appUser +
                '}';
    }

    public void copyValues(Comment comment){
        this.text = comment.text;
        this.appUser = comment.appUser;
    }
}