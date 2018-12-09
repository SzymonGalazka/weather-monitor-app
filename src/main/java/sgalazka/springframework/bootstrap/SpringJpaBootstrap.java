package sgalazka.springframework.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sgalazka.springframework.domain.Product;
import sgalazka.springframework.domain.Role;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.repositories.ProductRepository;
import sgalazka.springframework.services.RoleService;
import sgalazka.springframework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private ProductRepository productRepository;
	private UserService userService;
	private RoleService roleService;

	private Logger log = LoggerFactory.getLogger(SpringJpaBootstrap.class);

	@Autowired
	public void setProductRepository(ProductRepository productRepository) {
		this.productRepository = productRepository;
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
		loadProducts();
		loadUsers();
		loadRoles();
		assignUsersToUserRole();
		assignUsersToAdminRole();
	}

	private void loadProducts() {
		Product shirt = new Product();
		shirt.setDescription("Spring Framework Guru Shirt");
		shirt.setPrice(new BigDecimal("18.95"));
		shirt.setImageUrl("https://springframework.sgalazka/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
		shirt.setProductId("235268845711068308");
		productRepository.save(shirt);

		log.info("Saved Shirt - id: " + shirt.getId());

		Product mug = new Product();
		mug.setDescription("Spring Framework Guru Mug");
		mug.setImageUrl("https://springframework.sgalazka/wp-content/uploads/2015/04/spring_framework_guru_coffee_mug-r11e7694903c348e1a667dfd2f1474d95_x7j54_8byvr_512.jpg");
		mug.setProductId("168639393495335947");
		mug.setPrice(new BigDecimal("11.95"));
		productRepository.save(mug);

		log.info("Saved Mug - id:" + mug.getId());
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


