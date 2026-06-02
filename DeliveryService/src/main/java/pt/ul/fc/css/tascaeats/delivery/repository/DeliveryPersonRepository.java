package pt.ul.fc.css.tascaeats.delivery.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;

@Repository
public interface DeliveryPersonRepository
    extends JpaRepository<DeliveryPerson, Long>, JpaSpecificationExecutor<DeliveryPerson> {

  Optional<DeliveryPerson> findByUsername(String username);
  Optional<DeliveryPerson> findFirstByStatus(pt.ul.fc.css.tascaeats.delivery.model.DeliveryPersonStatus status);
}
