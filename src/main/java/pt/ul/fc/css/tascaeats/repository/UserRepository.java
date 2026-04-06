package pt.ul.fc.css.tascaeats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.tascaeats.entities.AUser;

@Repository
public interface UserRepository extends JpaRepository<AUser, Long> {
    // para ter em mente
    // Já tens de graça:
    // save(entity)
    // findById(id)
    // findAll()
    // deleteById(id)
    // existsById(id)
    // count()
    Optional<AUser> findByNifNumber(String nif);
    Optional<AUser> findByUsername(String username);

}