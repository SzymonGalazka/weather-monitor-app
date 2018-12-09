package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping()
	public List<User> getAllUsers() {
		return userService.getAll();
	}

	@GetMapping("/{userId}")
	public User getUser(@PathVariable(value = "userId") Integer userId) {
		return userService.findByUserId(userId);
	}

}
