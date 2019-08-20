package de.mymunch.code.repositories;

import de.mymunch.code.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  List<User> findAll();

  Optional<User> findByEmail(String email);
}
