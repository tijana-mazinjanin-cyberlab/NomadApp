package model;

import jakarta.persistence.*;
import model.enums.NotificationType;
import model.enums.UserType;

import java.util.*;

@Entity
@DiscriminatorValue("host")
public class Host extends AppUser{

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="notification-type")
    @Column(name="enabled")
    @CollectionTable(name = "notification_preferences",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Map<NotificationType, Boolean> notificationPreferences;


    public Host(){
        super();
    }

    public Host(Map<NotificationType, Boolean> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

//    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "host")
//    List<Accommodation> accommodations;
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.HOST);
    }


    public Map<NotificationType, Boolean> getNotificationPreferences() {
        if(!notificationPreferences.containsKey(NotificationType.NEW_ACCOMMODATION_RATING)) {
            notificationPreferences.put(NotificationType.NEW_ACCOMMODATION_RATING, true);
        }
        if(!notificationPreferences.containsKey(NotificationType.NEW_RATING)) {
            notificationPreferences.put(NotificationType.NEW_RATING, true);
        }
        if(!notificationPreferences.containsKey(NotificationType.NEW_RESERVATION)) {
            notificationPreferences.put(NotificationType.NEW_RESERVATION, true);
        }
        if(!notificationPreferences.containsKey(NotificationType.RESERVATION_CANCELED)) {
            notificationPreferences.put(NotificationType.RESERVATION_CANCELED, true);
        }
        return notificationPreferences;
    }

    public void setNotificationPreferences(Map<NotificationType, Boolean> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }
}
