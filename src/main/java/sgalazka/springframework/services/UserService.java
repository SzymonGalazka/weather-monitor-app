package sgalazka.springframework.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sgalazka.springframework.common.pojo.UserCreds;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;

import java.util.List;

public interface UserService extends CRUDService<User> {

	List<User> getAll();

	User findByUserId(Integer userId);

	User findByUsername(String username);

	String registerUserAndGetName(UserCreds userCreds);

	boolean checkIfAdmin();

	Page<User> listAllWithoutAdmin(Pageable pageable);
}
