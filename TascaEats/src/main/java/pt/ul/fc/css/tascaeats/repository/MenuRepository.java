package pt.ul.fc.css.tascaeats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.tascaeats.entities.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {}
