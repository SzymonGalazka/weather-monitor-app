package sgalazka.springframework.services;

import sgalazka.springframework.common.pojo.UserCreds;
import sgalazka.springframework.domain.User;

import java.util.List;

public interface UserService extends CRUDService<User> {

	List<User> getAll();

	User findByUserId(Integer userId);

	User findByUsername(String username);

	String registerUserAndGetName(UserCreds userCreds);

	boolean checkIfAdmin();

	List<User> listAllWithoutAdmin();
}
