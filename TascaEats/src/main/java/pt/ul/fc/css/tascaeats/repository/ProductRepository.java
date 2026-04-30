package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByIdAndRestaurantId(Long productId, Long restaurantId);

  List<Product> findAllByRestaurantId(Long restaurantId);

  List<Product> findByDesignationContainingIgnoreCase(String designation);
  List<Product> findByDescriptionContainingIgnoreCase(String description);
  List<Product> findByAvailabilityContainingIgnoreCase(ProductStatus availability);

  @Query("""
      SELECT p
      FROM Order o
      JOIN o.products p
      GROUP BY p
      HAVING COUNT(p) >= :timesOrdered
  """)
  List<Product> findByTimesOrdered(@Param("timesOrdered") int timesOrdered);
}
