package DTO;

import model.enums.NotificationType;

import java.util.Date;

public class NotificationDTO {
    private String text;
    private String title;
    private Long targetAppUser;
    private Date date;
    private NotificationType notificationType;

    public NotificationDTO(String text, String title, Long targetAppUser, Date date, NotificationType notificationType) {
        this.text = text;
        this.title = title;
        this.targetAppUser = targetAppUser;
        this.date = date;
        this.notificationType = notificationType;
    }
    public NotificationDTO(){}
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTargetAppUser() {
        return targetAppUser;
    }

    public void setTargetAppUser(Long targetAppUser) {
        this.targetAppUser = targetAppUser;
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
