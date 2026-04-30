package pt.ul.fc.css.tascaeats.repository;

import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.tascaeats.entities.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  List<Restaurant> findByAddressCityContainingIgnoreCaseAndAddressStreetContainingIgnoreCaseAndAddressPostalCodeContainingIgnoreCase(String city, String street, String postalCode);

  List<Restaurant> findByDesignationContainingIgnoreCase(String name);

  boolean existsByAdminId(Long id);

  boolean existsByMenuId(Long id);

  List<Restaurant> findByMenuCuisineContainingIgnoreCase(String cuisine);

  @Query("SELECT r FROM Restaurant r WHERE SIZE(r.orders) = :orderNumber")
  List<Restaurant> findByOrderNumber(@Param("orderNumber") int orderNumber);

  @Query("SELECT r FROM Restaurant r WHERE SIZE(r.reviews) = :reviewNumber")
  List<Restaurant> findByReviewNumber(@Param("reviewNumber") int reviewNumber);

  @Query("SELECT r FROM Restaurant r WHERE :activeHour BETWEEN r.openHours AND r.closeHours")
  List<Restaurant> findByActiveHours(@Param("activeHour") LocalTime activeHour);

  @Query("""
      SELECT r FROM Restaurant r
      JOIN r.products p
      GROUP BY r
      HAVING AVG(p.price) <= :average
  """)
  List<Restaurant> findByAveragePrice(@Param("average") double average);
}
