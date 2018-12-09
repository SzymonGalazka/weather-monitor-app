package sgalazka.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.services.WeatherService;
import sgalazka.springframework.services.WeatherServiceImpl;
import sgalazka.springframework.services.security.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
public class WeatherController {


	@Autowired
	private WeatherServiceImpl weatherService;

//	@PostMapping
//	public void saveWeather(@RequestBody Weather weather) {
//		weatherService.saveWeather(weather);
//	}

	@GetMapping("/users/{userId}/weather")
	public List<Weather> getAllWeatherForUserId(@PathVariable(value = "userId") Integer userId) {
		return weatherService.findAllByUserId(userId);
	}

	@PostMapping("/users/{userId}/weather")
	public void createWeather(@PathVariable(value = "userId") Integer userId,
							  @Valid @RequestBody Weather weather) {
		weatherService.saveWeather(weather, userId);
	}

	@DeleteMapping("/users/{userId}/weather/{weatherId}")
	public void deleteWeather(@PathVariable(value = "userId") Integer userId) {
		weatherService.deleteAllWeatherForUserId(userId);
	}

	//todo
//	@PostMapping("/users/{userId}/weather/current")
//	public ModelAndView getCurrentWeather(
//			@PathVariable(value = "userId") Long userId,
//			@RequestParam("city") String city) {
//		ModelAndView modelAndView = new ModelAndView("/home");
//		modelAndView.addObject("currentWeather", weatherService.getWeatherForCity(city, userId));
//
//		return modelAndView;
//	}
	@PostMapping("/users/me/weather/current")
	public Weather getCurrentWeather(
			@RequestParam("city") String city) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Integer userId = userDetails.getUserId();
		System.out.println("userId: " + userId);
		return weatherService.getWeatherForCity(city, userId);
	}
}
