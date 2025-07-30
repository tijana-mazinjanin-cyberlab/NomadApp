package Services;

import Repositories.IRepository;
import Repositories.UserReportRepository;
import model.UserReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserReportService implements IService<UserReport, Long> {

    @Autowired
    private UserReportRepository userReportRepository;

    @Override
    public Collection<UserReport> findAll() {
        return userReportRepository.findAll();
    }

    @Override
    public UserReport findOne(Long id) {
        return userReportRepository.findOneById(id);
    }

    @Override
    public void create(UserReport userReport) {
        userReportRepository.save(userReport);
    }

    @Override
    public void update(UserReport userReport) {
        userReportRepository.save(userReport);
    }

    @Override
    public void delete(Long id) {
        userReportRepository.deleteById(id);
    }

}
