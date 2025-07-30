package model;

import jakarta.persistence.*;
import model.enums.NotificationType;
import model.enums.UserType;

import java.util.*;

@Entity
@DiscriminatorValue("guest")
public class Guest extends AppUser{
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "guest")
//    List<Reservation> reservations;

//    @Column(columnDefinition = "boolean default true")
//    private boolean requestResponseEnabled;

    @ElementCollection
    @MapKeyColumn(name="notification-type")
    @Column(name="enabled")
    @CollectionTable(name = "notification_preferences",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Map<NotificationType, Boolean> notificationPreferences;


    Long cancellationNumber;

    public Guest() {
        super();
    }

    public Guest(Map<NotificationType, Boolean> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    public void increaseNumber(){
        this.cancellationNumber++;
    }

//    public List<Reservation> getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(List<Reservation> reservations) {
//        this.reservations = reservations;
//    }

    public Long getCancellationNumber() {
        return cancellationNumber;
    }

    public void setCancellationNumber(long cancellationNumber) {
        this.cancellationNumber = cancellationNumber;
    }

    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.GUEST);
    }


    public Map<NotificationType, Boolean> getNotificationPreferences() {
        if(!notificationPreferences.containsKey(NotificationType.REQUEST_RESPONSE)) {
            notificationPreferences.put(NotificationType.REQUEST_RESPONSE, true);
        }
        return notificationPreferences;
    }

    public void setNotificationPreferences(Map<NotificationType, Boolean> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }
}
