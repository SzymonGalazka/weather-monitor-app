package sgalazka.springframework.services;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sgalazka.springframework.common.pojo.UserCreds;
import sgalazka.springframework.domain.Role;
import sgalazka.springframework.domain.User;

import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.repositories.UserRepository;
import sgalazka.springframework.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sgalazka.springframework.services.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.toList;


@Service
@Profile("springdatajpa")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private EncryptionService encryptionService;

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}


	@Override
	public List<?> listAll() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	@Override
	public User getById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public User saveOrUpdate(User domainObject) {
		if (domainObject.getPassword() != null) {
			domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
		}
		return userRepository.save(domainObject);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		userRepository.delete(id);
	}

	@Override
	public List<User> getAll() {
		return toList(userRepository.findAll());
	}

	@Override
	public User findByUserId(Integer userId) {
		return userRepository.findById(userId);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public String registerUserAndGetName(UserCreds userCreds) {
		User newUser = new User();
		newUser.setUsername(userCreds.getUsername());
		newUser.setEncryptedPassword(encryptionService.encryptString(userCreds.getPassword()));

		Role role = roleService.getById(1);
		newUser.addRole(role);
		userRepository.save(newUser);
		roleService.saveOrUpdate(role);

		return newUser.getUsername();
	}

	@Override
	public boolean checkIfAdmin() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails.getUsername().toUpperCase().equals("ADMIN");
	}

	@Override
	public List<User> listAllWithoutAdmin() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		users.stream()
				.filter(user -> user.getUsername().toUpperCase().equals("ADMIN"))
				.findFirst()
				.map(users::remove);
		return users;
	}

}
