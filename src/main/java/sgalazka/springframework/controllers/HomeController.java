package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.services.UserService;
import sgalazka.springframework.services.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

	@Value("${spring.application.name}")
	String appName;

	@Autowired
	UserService userService;

	@GetMapping
	String home(Model model,
				@RequestParam("page") Optional<Integer> page,
				@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);
		model.addAttribute("appName", appName);
		if (userService.checkIfAdmin()) {

			Page<User> userPage = userService.listAllWithoutAdmin
					(new PageRequest(currentPage - 1, pageSize));

			model.addAttribute("userPage", userPage);

			int totalPages = userPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
						.boxed()
						.collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "home";
	}
}
