package pt.ul.fc.css.tascaeats.repository;

import pt.ul.fc.css.tascaeats.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByIdAndRestaurantId(Long productId, Long restaurantId);
}
