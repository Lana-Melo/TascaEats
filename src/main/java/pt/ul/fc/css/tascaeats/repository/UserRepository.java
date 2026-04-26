package pt.ul.fc.css.tascaeats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;

@Repository
public interface UserRepository extends JpaRepository<AUser, Long> {
    Optional<AUser> findByUsername(String username);
    Optional<DeliveryPerson> findFirstByStatus(DeliveryPersonStatus status);

}