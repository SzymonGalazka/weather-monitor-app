package sgalazka.springframework.controllers;

import org.springframework.web.bind.annotation.RequestMethod;
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
public class AuthorizationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegister() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView signUp(UserCreds userCreds) {
		ModelAndView mav = new ModelAndView("login");

		if (Objects.nonNull(userRepository.findByUsername(userCreds.getUsername()))) {
			throw new UserAlreadyInUseException("User already registered!");
		}

		String registeredName = userService.registerUserAndGetName(userCreds);
		mav.addObject("registeredName", registeredName);
		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

}
