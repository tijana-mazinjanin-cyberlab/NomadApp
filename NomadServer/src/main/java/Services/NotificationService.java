package Services;

import Repositories.IRepository;
import Repositories.NotificationRepository;
import model.AppUser;
import model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class NotificationService implements IService<Notification, Long> {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public Collection<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void create(Notification object) {
        notificationRepository.save(object);
    }

    @Override
    public Notification findOne(Long id) {
        return notificationRepository.findOneById(id);
    }

    @Override
    public void update(Notification object) {
        notificationRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    public Collection<Notification> getAllForUSer(Long id) {
        AppUser user = this.userService.findOne(id);

        return notificationRepository.findAllByTargetAppUser_Id(id);
    }
}
