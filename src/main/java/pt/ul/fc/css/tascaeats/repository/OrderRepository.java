package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.tascaeats.entities.Order;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCode(Long id);
    Optional<List<Order>> findByClientId(Long id);
    //Optional<List<Order>> findByRestaurant(Long id);
}
