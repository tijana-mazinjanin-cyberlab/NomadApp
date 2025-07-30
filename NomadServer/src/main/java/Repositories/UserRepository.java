package Repositories;

import model.Admin;
import model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findOneById(Long id);

    AppUser findOneByUsername(String username);
}
