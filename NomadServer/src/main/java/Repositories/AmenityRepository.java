package Repositories;

import model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Amenity findOneById(Long id);
}
