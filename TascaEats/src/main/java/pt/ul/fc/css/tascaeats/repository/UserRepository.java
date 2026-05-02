package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@Repository
public interface UserRepository
    extends JpaRepository<AUser, Long>, JpaSpecificationExecutor<AUser> {
  Optional<AUser> findByUsername(String username);

  Optional<DeliveryPerson> findFirstByStatus(DeliveryPersonStatus status);

  List<AUser> findAllByNameContainingIgnoreCase(String name);

  @Query("SELECT c FROM Client c")
  List<Client> findAllClients();

  @Query("SELECT a FROM Admin a")
  List<Admin> findAllAdmins();

  @Query("SELECT d FROM DeliveryPerson d")
  List<DeliveryPerson> findAllDeliveryPersons();

  @Query(
      """
          SELECT c FROM Client c, Order o
          WHERE o.client = c
          GROUP BY c
          HAVING COUNT(o) >= :orderNumber
      """)
  List<Client> findClientsWithOrderNumber(@Param("orderNumber") int orderNumber);

  @Query(
      """
          SELECT d FROM DeliveryPerson d, Order o
          WHERE o.deliveryPerson = d AND o.status = :status
          GROUP BY d
          HAVING COUNT(o) >= :deliveryNumber
      """)
  List<DeliveryPerson> findDeliveryPersonsWithDeliveryNumber(
      @Param("deliveryNumber") int deliveryNumber, @Param("status") OrderStatus status);
}
