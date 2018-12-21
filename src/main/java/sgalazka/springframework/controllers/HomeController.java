package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sgalazka.springframework.services.UserService;
import sgalazka.springframework.services.security.UserDetailsImpl;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

	@Value("${spring.application.name}")
	String appName;

	@Autowired
	UserService userService;

	@GetMapping
	String home(Model model) {
		model.addAttribute("appName", appName);
		if (userService.checkIfAdmin()) {
			model.addAttribute("users", userService.listAllWithoutAdmin());
		} else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Integer userId = userDetails.getUserId();
			model.addAttribute("user", userService.getById(userId));
		}
		return "home";

	}
}
