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
import org.springframework.web.bind.annotation.ResponseBody;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.services.UserService;
import sgalazka.springframework.services.WeatherService;
import sgalazka.springframework.services.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = {"/history"})
public class HistoryController {

	@Value("${spring.application.name}")
	String appName;

	@Autowired
	UserService userService;

	@Autowired
	WeatherService weatherService;

	@GetMapping
	String history(Model model,
				   @RequestParam("page") Optional<Integer> page,
				   @RequestParam("size") Optional<Integer> size,
				   @RequestParam("city") Optional<String> city,
				   @RequestParam("dates") Optional<String> dates) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(10);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Integer userId = userDetails.getUserId();

		Page<Weather> weatherPage = weatherService.listPaginated
				(new PageRequest(currentPage - 1, pageSize), userId, city, dates);
		model.addAttribute("weatherPage", weatherPage);
		if (weatherPage.getTotalElements() > 0) {
			model.addAttribute("weatherStats", weatherService.calculateStats(weatherPage));
		}
		int totalPages = weatherPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("appName", appName);
		return "history";
	}
}
