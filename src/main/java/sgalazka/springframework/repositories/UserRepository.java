package sgalazka.springframework.repositories;

import org.springframework.data.repository.CrudRepository;
import sgalazka.springframework.domain.User;


public interface UserRepository extends CrudRepository<User, Integer> {

	User findById(Integer userId);

	User findByUsername(String username);
}
