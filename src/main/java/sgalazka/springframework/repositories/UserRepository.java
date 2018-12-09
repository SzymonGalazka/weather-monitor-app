package sgalazka.springframework.repositories;

import sgalazka.springframework.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findById(Integer userId);

	User findByUsername(String username);
}
