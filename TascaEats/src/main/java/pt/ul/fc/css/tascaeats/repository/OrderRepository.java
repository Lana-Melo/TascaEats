package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByClientId(Long clientId);

  boolean existsByProductItemsId(Long productId);

  boolean existsByClientId(Long clientId);

  boolean existsByClientIdAndStatusNotIn(Long clientId, List<OrderStatus> status);

  boolean existsByDeliveryPersonId(Long id);

  boolean existsByDeliveryPersonIdAndStatusNotIn(Long id, List<OrderStatus> of);

  boolean existsByProductItemsIdAndStatusNotIn(Long productId, List<OrderStatus> of);
}
