package Repositories;

import model.HostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostCommentRepository extends JpaRepository<HostComment, Long> {

    HostComment findOneById(Long id);

}
