package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByIdAndRestaurantId(Long productId, Long restaurantId);

  Optional<List<Product>> findAllByRestaurantId(Long restaurantId);
}
