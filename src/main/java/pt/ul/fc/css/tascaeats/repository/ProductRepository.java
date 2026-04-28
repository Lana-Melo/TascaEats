package pt.ul.fc.css.tascaeats.repository;

import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByIdAndRestaurantId(Long productId, Long restaurantId);

    Optional <List<Product>> findAllByRestaurantId(Long restaurantId);
    List<Product> findAllByDesignation(String designation);
    List<Product> findAllByDescription(String description);
    List<Product> findAllByAvailability(ProductStatus availability);

    @Query("""
        SELECT p
        FROM Order o
        JOIN o.products p
        GROUP BY p
        HAVING COUNT(p) = :timesOrdered
    """)
    List<Product> findByTimesOrdered(@Param("timesOrdered") int timesOrdered);
}
