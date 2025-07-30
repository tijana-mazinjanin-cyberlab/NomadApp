package Services;

import Repositories.IRepository;
import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentService implements IService<Comment, Long> {

    @Autowired
    private IRepository<Comment, Long> accommodationRepository;

    @Override
    public Collection<Comment> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Comment findOne(Long id) {
        return accommodationRepository.findOne(id);
    }

    @Override
    public void create(Comment comment) {
        accommodationRepository.create(comment);
    }

    @Override
    public void update(Comment comment) {
        accommodationRepository.update(comment);
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.delete(id);
    }

}
