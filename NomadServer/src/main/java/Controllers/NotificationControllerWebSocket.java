package Controllers;

import Services.NotificationService;
import Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.AppUser;
import model.Guest;
import model.Host;
import model.Notification;
import model.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NotificationControllerWebSocket {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @MessageMapping("/send/message")
    public Map<String, String> broadcastNotification(String message) {
        Map<String, String> messageConverted = parseMessage(message);
        Notification notification = convertToEntity(messageConverted);
        Map<NotificationType, Boolean> notificationPreferences = new HashMap<>();

        AppUser user = notification.getTargetUser();
        if(user instanceof Host) {
            Host host = (Host) user;
            notificationPreferences = host.getNotificationPreferences();
        } else if(user instanceof Host) {
            Guest guest = (Guest) user;
            notificationPreferences = guest.getNotificationPreferences();
        }

        if (messageConverted != null) {
            if (messageConverted.containsKey("targetAppUser") && messageConverted.get("targetAppUser") != null
                    && !messageConverted.get("targetAppUser").equals("")) {

                if(notificationPreferences.containsKey(notification.getNotificationType())) {
                    if(notificationPreferences.get(notification.getNotificationType())) {
                        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("targetAppUser"),
                                messageConverted);

                    }
                }
                notificationService.create(notification);
            }
        }

        return messageConverted;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retVal;

        try {
            retVal = mapper.readValue(message, Map.class); // parsiranje JSON stringa
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }

    public Notification convertToEntity(Map<String, String> message) {
        Notification notification = new Notification();

        notification.setTitle(message.get("title"));
        notification.setText(message.get("text"));
        notification.setDate(this.convertToDate(message.get("date")));
        notification.setNotificationType(NotificationType.valueOf(message.get("notificationType")));
        notification.setTargetUser(userService.findOne(Long.parseLong(message.get("targetAppUser"))));


        return notification;
    }

    private Date convertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            Date date = dateFormat.parse(dateString);
            System.out.println("Parsed Date: " + date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
