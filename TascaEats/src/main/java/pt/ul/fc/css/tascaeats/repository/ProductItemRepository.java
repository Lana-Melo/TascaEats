package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
  boolean existsByIdAndRestaurantId(Long productId, Long restaurantId);

  List<ProductItem> findAllByRestaurantId(Long restaurantId);

  List<ProductItem> findByProductDesignationContainingIgnoreCase(String designation);

  List<ProductItem> findByProductDescriptionContainingIgnoreCase(String description);

  List<ProductItem> findByAvailability(ProductStatus availability);

  @Query(
      """
          SELECT p
          FROM Order o
          JOIN o.productItems p
          GROUP BY p
          HAVING COUNT(p) >= :timesOrdered
      """)
  List<ProductItem> findByTimesOrdered(@Param("timesOrdered") int timesOrdered);
}
