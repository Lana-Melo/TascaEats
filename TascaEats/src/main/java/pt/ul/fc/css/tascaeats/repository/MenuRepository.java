package pt.ul.fc.css.tascaeats.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
  List<Menu> findByNameContainingIgnoreCase(String name);

  @Query("SELECT m FROM Menu m WHERE SIZE(m.products) = :productNumber")
  List<Menu> findByProductNumber(@Param("productNumber") int productNumber);

  @Query(
      """
        SELECT m FROM Menu m
        JOIN m.products p
        GROUP BY m
        HAVING AVG(p.price) <= :average
      """)
  List<Menu> findByAveragePrice(@Param("average") double average);

  @Modifying
  @Query(
      value = "DELETE FROM menu_products WHERE menu_id = :menuId AND product_id = :productId",
      nativeQuery = true)
  void deleteMenuProductRelation(@Param("menuId") Long menuId, @Param("productId") Long productId);
}
