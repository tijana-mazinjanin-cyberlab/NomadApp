package Repositories;

import model.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface HostRepository extends JpaRepository <Host, Long> {
}
