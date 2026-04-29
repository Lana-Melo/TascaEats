package pt.ul.fc.css.tascaeats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByName(String name);

    @Query ("SELECT m FROM Menu m WHERE SIZE(m.products) = :productNumber")
    List<Menu> findByProductNumber(int product);

    @Query("""
      SELECT m FROM Menu m
      JOIN m.products p
      GROUP BY m
      HAVING AVG(p.price) = :average
    """)
    List<Menu> findByAveragePrice(double average);
}
