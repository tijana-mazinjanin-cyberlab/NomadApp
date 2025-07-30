package Services;

import Repositories.CommentReportRepository;
import Repositories.IRepository;
import model.Comment;
import model.CommentReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentReportService implements IService<CommentReport, Long> {

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Override
    public Collection<CommentReport> findAll() {
        return commentReportRepository.findAll();
    }

    @Override
    public CommentReport findOne(Long id) {
        return commentReportRepository.findOneById(id);
    }

    @Override
    public void create(CommentReport commentReport) {
        commentReportRepository.save(commentReport);
    }

    @Override
    public void update(CommentReport commentReport) {
        commentReportRepository.save(commentReport);
    }

    @Override
    public void delete(Long id) {
        commentReportRepository.deleteById(id);
    }

}
