package pt.ul.fc.css.tascaeats.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.delivery.model.Delivery;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryStatus;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
  boolean existsByDeliveryPersonId(Long deliveryPersonId);

  boolean existsByDeliveryPersonIdAndStatusNotIn(
      Long deliveryPersonId, List<DeliveryStatus> statuses);

  List<Delivery> findByDeliveryPersonId(Long deliveryPersonId);
}
