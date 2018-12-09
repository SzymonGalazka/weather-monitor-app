package sgalazka.springframework.repositories;

import sgalazka.springframework.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer>{
}
