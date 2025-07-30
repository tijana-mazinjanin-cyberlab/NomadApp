package Services;

import Repositories.AccommodationCommentRepository;
import Repositories.IRepository;
import model.Accommodation;
import model.AccommodationComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class AccommodationCommentService implements IService<AccommodationComment, Long> {

    @Autowired
    private AccommodationCommentRepository accommodationCommentRepository;

    @Override
    public Collection<AccommodationComment> findAll() {
        return accommodationCommentRepository.findAll();
    }

    @Override
    public void create(AccommodationComment object) {
        accommodationCommentRepository.save(object);
    }

    @Override
    public AccommodationComment findOne(Long id) {
        return accommodationCommentRepository.findOneById(id);
    }

    @Override
    public void update(AccommodationComment object) {
        accommodationCommentRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        accommodationCommentRepository.deleteById(id);
    }
}
