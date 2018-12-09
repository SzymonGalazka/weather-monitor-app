package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

	@Value("${spring.application.name}")
	String appName;

	@GetMapping
	String home(Model model) {
		model.addAttribute("appName", appName);
		return "home";
	}
}
