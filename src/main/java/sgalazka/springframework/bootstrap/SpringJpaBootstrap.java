package sgalazka.springframework.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sgalazka.springframework.domain.Product;
import sgalazka.springframework.domain.Role;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.repositories.ProductRepository;
import sgalazka.springframework.repositories.UserRepository;
import sgalazka.springframework.services.RoleService;
import sgalazka.springframework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import sgalazka.springframework.services.WeatherService;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private WeatherService weatherService;
	private UserService userService;
	private RoleService roleService;

	private Logger log = LoggerFactory.getLogger(SpringJpaBootstrap.class);

	@Autowired
	public void setWeatherService(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		loadUsers();
		loadRoles();
		loadHistory();
		assignUsersToUserRole();
		assignUsersToAdminRole();
	}

	private void loadHistory() {
		Weather weather = new Weather();
		weather.setCity("Tokio");
		weather.setMain("Clear");
		weather.setDescription("Clear Sky");
		weather.setTemp(5.0);
		weather.setTempMin(2.0);
		weather.setTempMax(7.4);

		weatherService.saveWeather(weather, 2);
		weatherService.saveWeather(weather, 4);

		Weather weather2 = new Weather();
		weather2.setCity("Warsaw");
		weather2.setMain("Clouds");
		weather2.setDescription("Heavy Rain");
		weather2.setTemp(2.0);
		weather2.setTempMin(-1.3);
		weather2.setTempMax(3.4);

		weatherService.saveWeather(weather2, 2);
		weatherService.saveWeather(weather2, 3);

	}

	private void loadUsers() {

		User user1 = new User();
		user1.setUsername("admin");
		user1.setPassword("admin");
		userService.saveOrUpdate(user1);

		User user2 = new User();
		user2.setUsername("szymon");
		user2.setPassword("123456");
		userService.saveOrUpdate(user2);

		User user3 = new User();
		user3.setUsername("piotrek");
		user3.setPassword("123456");
		userService.saveOrUpdate(user3);

		User user4 = new User();
		user4.setUsername("kuba");
		user4.setPassword("123456");
		userService.saveOrUpdate(user4);

		User user5 = new User();
		user5.setUsername("tomasz");
		user5.setPassword("123456");
		userService.saveOrUpdate(user5);
	}

	private void loadRoles() {
		Role role = new Role();
		role.setRole("USER");
		roleService.saveOrUpdate(role);
		log.info("Saved role" + role.getRole());
		Role adminRole = new Role();
		adminRole.setRole("ADMIN");
		roleService.saveOrUpdate(adminRole);
		log.info("Saved role" + adminRole.getRole());
	}

	private void assignUsersToUserRole() {
		List<Role> roles = (List<Role>) roleService.listAll();
		List<User> users = (List<User>) userService.listAll();

		roles.forEach(role -> {
			if (role.getRole().equalsIgnoreCase("USER")) {
				users.forEach(user -> {
					if (!user.getUsername().equals("admin")) {
						user.addRole(role);
						userService.saveOrUpdate(user);
					}
				});
			}
		});
	}

	private void assignUsersToAdminRole() {
		List<Role> roles = (List<Role>) roleService.listAll();
		List<User> users = (List<User>) userService.listAll();

		roles.forEach(role -> {
			if (role.getRole().equalsIgnoreCase("ADMIN")) {
				users.forEach(user -> {
					if (user.getUsername().equals("admin")) {
						user.addRole(role);
						userService.saveOrUpdate(user);
					}
				});
			}
		});
	}
}


