package Services;

import Repositories.HostCommentRepository;
import Repositories.IRepository;
import Repositories.ReservationDateRepository;
import model.Accommodation;
import model.HostComment;
import model.ReservationDate;
import model.enums.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class HostCommentService implements IService<HostComment, Long> {

    @Autowired
    private HostCommentRepository hostCommentRepository;

    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Override
    public Collection<HostComment> findAll() {
        return hostCommentRepository.findAll();
    }

    @Override
    public void create(HostComment object) {
        hostCommentRepository.save(object);
    }

    @Override
    public HostComment findOne(Long id) {
        return hostCommentRepository.findOneById(id);
    }

    @Override
    public void update(HostComment object) {
        hostCommentRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        hostCommentRepository.deleteById(id);
    }

    public Boolean canRate(Long hostId, Long guestId) {
        Collection<ReservationDate> dates = reservationDateRepository.findAllByAccommodation_Host_IdAndReservationGuest_Id(hostId, guestId);
        if(dates.isEmpty()){
            return false;
        }
        for (ReservationDate date: dates){
            if (date.getReservation().getStatus() == ReservationStatus.ACCEPTED){
                return true;
            }
        }
        return false;
    }
}
