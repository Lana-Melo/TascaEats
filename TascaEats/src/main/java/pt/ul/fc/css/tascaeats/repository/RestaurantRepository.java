package pt.ul.fc.css.tascaeats.repository;

import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Restaurant;

@Repository
public interface RestaurantRepository
    extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

  List<Restaurant> findByAddressCityContainingIgnoreCase(String city);

  List<Restaurant> findByAddressStreetContainingIgnoreCase(String street);

  List<Restaurant> findByAddressPostalCodeContainingIgnoreCase(String postalCode);

  List<Restaurant> findByDesignationContainingIgnoreCase(String name);

  List<Restaurant> findByAdminId(Long adminId);

  boolean existsByAdminId(Long id);

  boolean existsByMenuId(Long id);

  List<Restaurant> findByMenuCuisineContainingIgnoreCase(String cuisine);

  @Query("SELECT r FROM Restaurant r WHERE SIZE(r.orders) = :orderNumber")
  List<Restaurant> findByOrderNumber(@Param("orderNumber") int orderNumber);

  @Query("SELECT r FROM Restaurant r WHERE SIZE(r.reviews) = :reviewNumber")
  List<Restaurant> findByReviewNumber(@Param("reviewNumber") int reviewNumber);

  @Query("SELECT r FROM Restaurant r WHERE :activeHour BETWEEN r.openHours AND r.closeHours")
  List<Restaurant> findByActiveHours(@Param("activeHour") LocalTime activeHour);

  @Query(
      """
          SELECT r FROM Restaurant r
          JOIN r.products p
          GROUP BY r
          HAVING AVG(p.price) <= :average
      """)
  List<Restaurant> findByAveragePrice(@Param("average") double average);
}
