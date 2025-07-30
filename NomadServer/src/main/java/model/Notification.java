package model;

import jakarta.persistence.*;
import model.enums.NotificationType;

import java.util.Date;

@Entity
@Table (name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne (fetch = FetchType.EAGER)
    private AppUser targetAppUser;
    private String text;
    private String title;
    private Date date;
    private NotificationType notificationType;

    public Notification(String text, AppUser targetAppUser, String title, Date date, NotificationType notificationType) {
        this.text = text;
        this.targetAppUser = targetAppUser;
        this.title = title;
        this.date = date;
        this.notificationType = notificationType;
    }
    public Notification(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AppUser getTargetUser() {
        return targetAppUser;
    }

    public void setTargetUser(AppUser targetAppUser) {
        this.targetAppUser = targetAppUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void copyValues(Notification notification){
        this.title = notification.title;
        this.text = notification.text;
        this.targetAppUser = notification.targetAppUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
