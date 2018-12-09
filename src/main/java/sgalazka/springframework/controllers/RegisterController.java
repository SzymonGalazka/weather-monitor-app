package sgalazka.springframework.controllers;

import sgalazka.springframework.common.exceptions.UserAlreadyInUseException;
import sgalazka.springframework.common.pojo.UserCreds;
import sgalazka.springframework.repositories.UserRepository;
import sgalazka.springframework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping
	public String showRegister() {
		return "register";
	}

	@PostMapping
	public ModelAndView signUp(UserCreds userCreds) {
		if (Objects.nonNull(userRepository.findByUsername(userCreds.getUsername()))) {
			throw new UserAlreadyInUseException("User already registered!");
		}

		String registeredName = userService.registerUserAndGetName(userCreds);
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("registeredName", registeredName);
		return mav;
	}
}
