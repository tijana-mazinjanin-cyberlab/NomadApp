package Repositories;

import model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findOneById(Long id);
    Collection<Notification> findAllByTargetAppUser_Id(Long id);
}
