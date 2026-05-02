package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByIdAndMenuId(Long productId, Long menuId);

  List<Product> findAllByMenuId(Long menuId);

  List<Product> findByDesignationContainingIgnoreCase(String designation);

  List<Product> findByDescriptionContainingIgnoreCase(String description);
}
