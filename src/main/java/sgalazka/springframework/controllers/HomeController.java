package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sgalazka.springframework.services.UserService;

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
		}
		return "home";

	}
}
