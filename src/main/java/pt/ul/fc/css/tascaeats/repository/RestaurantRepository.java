package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.tascaeats.entities.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByNif(Long id);
    Optional<List<Restaurant>> findByDesignationContaining(String name);
    Optional<List<Restaurant>> findByStatus(String city);
    Optional<List<Restaurant>> findByDesignationContainingOrAddressCityContaining(String designation, String city);
    Optional<List<Restaurant>> findByAddressCityContaining(String city);
}
