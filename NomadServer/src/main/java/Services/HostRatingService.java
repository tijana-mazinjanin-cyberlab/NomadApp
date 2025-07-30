package Services;

import Repositories.CommentReportRepository;
import Repositories.HostRatingRepository;
import Repositories.IRepository;
import Repositories.UserReportRepository;
import model.CommentReport;
import model.HostRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class HostRatingService implements IService<HostRating, Long> {

    @Autowired
    private HostRatingRepository hostRatingRepository;



    @Override
    public Collection<HostRating> findAll() {
        return hostRatingRepository.findAll();
    }

    @Override
    public void create(HostRating object) {
        hostRatingRepository.save(object);
    }

    @Override
    public HostRating findOne(Long id) {
        return hostRatingRepository.findOneById(id);
    }

    @Override
    public void update(HostRating object) {
        hostRatingRepository.save(object);
    }

    @Override
    public void delete(Long id) {

        hostRatingRepository.deleteById(id);
    }

    public Collection<HostRating> findAllRatingsForHost(Long id) {
        return hostRatingRepository.findAllByHost_Id(id);
    }
    public Long findForUserAndHost(Long userId, Long accommodationId) {
        HostRating rating;
        try{
            rating = hostRatingRepository.findOneByAppUser_IdAndHost_Id(userId, accommodationId);

        }catch (Exception e){    //This ensures backwards compatibility with versions of the database
            //from when we allowed multiple comments by the same user
            rating = (HostRating) hostRatingRepository.findAllByAppUser_IdAndHost_Id(userId, accommodationId).toArray()[0];
        }
        if(rating == null){
            return -1L;
        }
        return rating.getId();
    }
}
