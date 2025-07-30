package Repositories;

import model.AccommodationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationCommentRepository extends JpaRepository<AccommodationComment, Long> {
    AccommodationComment findOneById(Long id);
}
