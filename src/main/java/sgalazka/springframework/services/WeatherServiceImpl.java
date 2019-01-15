package sgalazka.springframework.services;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sgalazka.springframework.converters.DaysBetweenCalculator;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.repositories.UserRepository;
import sgalazka.springframework.repositories.WeatherRepository;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WeatherServiceImpl implements WeatherService {


	private final static Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
	private final static String WEATHER_FORECAST = "Dzisiaj W %s bedzie %s oraz %s. Temperatura to minimum %.2f^C, maksimum %.2f^C, czyli średnio %.2f^C.";

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private UserRepository userRepository;

	@Value("${weather_api.address}")
	private String apiAddress;


	@Override
	public List<Weather> findAllByUserId(Integer userId) {
		return toList(weatherRepository.findAllByUserId(userId));
	}

	@Override
	public List<Weather> findAllByCity(String city, Integer userId) {
		List<Weather> userWeather = findAllByUserId(userId);
		userWeather.stream().filter(w -> city.equals(w.getCity()))
				.findAny()
				.orElse(null);
		userWeather.forEach(System.out::println);
		return userWeather;
	}

	@Override
	public void saveWeather(Weather weather, Integer userId) {
		User user = userRepository.findById(userId);
//		.orElseThrow(() -> new EntityNotFoundException("UserID not found: " + username))
		weather.setUser(user);
		weather.setCity(convertWeatherLetters(weather.getCity()));
		weatherRepository.save(weather);
	}

	private String convertWeatherLetters(String city) {
		city = city.toLowerCase().replace("ą", "a").replace("ś", "s")
				.replace("ć", "c").replace("ż", "z")
				.replace("ź", "z").replace("ę", "e")
				.replace("ó", "o").replace("ł", "l");
		return city.substring(0, 1).toUpperCase() + city.substring(1);
	}

	//todo
	@Override
	public void deleteAllWeatherForUserId(Integer userId) {
		User user = userRepository.findById(userId);
//		.orElseThrow(() -> new EntityNotFoundException("UserID not found: " + userId));
	}

	@Override
	public Weather getWeatherForCity(String city, Integer userId) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> weatherResponse = restTemplate.getForEntity(String.format(apiAddress, city), String.class);
		Weather weather = parseWeather(weatherResponse.getBody(), city);
		saveWeather(weather, userId);
		logWeatherInfo(weather);
		return weather;
	}

	@Override
	public List<String> calculateStats(Page<Weather> weatherPage) {

		DecimalFormat df2 = new DecimalFormat(".##");

		OptionalDouble maxTemp = weatherPage.getContent().stream().mapToDouble(Weather::getTemp).max();
		OptionalDouble minTemp = weatherPage.getContent().stream().mapToDouble(Weather::getTemp).min();
		OptionalDouble avgTemp = weatherPage.getContent().stream().mapToDouble(Weather::getTemp).average();

		List<String> tempStats = new ArrayList<>();

		tempStats.add("Max temperature: " + df2.format(maxTemp.getAsDouble()) + " C");
		tempStats.add("Min temperature: " + df2.format(minTemp.getAsDouble()) + " C");
		tempStats.add("Average temperature: " + df2.format(avgTemp.getAsDouble()) + " C");

		return tempStats;
	}

	private void logWeatherInfo(Weather weather) {
		logger.warn(String.format(WEATHER_FORECAST, weather.getCity(), weather.getMain(), weather.getDescription(), weather.getTempMin(), weather.getTempMax(), weather.getTemp()));
	}

	private Weather parseWeather(String jsonResponse, String city) {
		JSONObject parsedWeather = new JSONObject(jsonResponse);
		JSONArray weatherArray = parsedWeather.getJSONArray("weather");
		JSONObject weatherObject = weatherArray.getJSONObject(0);
		String weatherMain = weatherObject.getString("main");
		String weatherDescription = weatherObject.getString("description");
		JSONObject tempObject = parsedWeather.getJSONObject("main");
		double temp = tempObject.getDouble("temp");
		double tempMin = tempObject.getDouble("temp_min");
		double tempMax = tempObject.getDouble("temp_max");
		return new Weather(city, weatherMain, weatherDescription, temp, tempMin, tempMax);
	}

	private static <T> List<T> toList(final Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	public Page<Weather> listPaginated(Pageable pageable, Integer userId, Optional<String> city, Optional<String> dates) {
		List<Weather> weathers = weatherRepository.findAllByUserId(userId);
		if (city.isPresent()) {
			weathers = weathers
					.stream()
					.filter(w -> w.getCity().equalsIgnoreCase(city.get()))
					.collect(Collectors.toList());
			weathers.forEach(System.out::println);
		} else if (dates.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String[] convertedDates = dates.get().split("-");

			LocalDate startDate = LocalDate.parse(convertedDates[0].replace(" ", ""), formatter);
			LocalDate endDate = LocalDate.parse(convertedDates[1].replace(" ", ""), formatter);
			System.out.println(startDate + " TO " + endDate);
			List<LocalDate> allDates = DaysBetweenCalculator.getDatesBetween(startDate, endDate);
			allDates.forEach(System.out::println);
			weathers.forEach(weather -> System.out.println(weather.getDateCreated()));
			weathers = weathers.stream()
					.filter(w -> allDates.toString().contains(w.getDateCreated().toString()))
					.collect(Collectors.toList());


		}
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Weather> list;

		if (weathers.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, weathers.size());
			list = weathers.subList(startItem, toIndex);
		}

		return new PageImpl<Weather>(list, new PageRequest(currentPage, pageSize), weathers.size());
	}


	@Override
	public List<?> listAll() {
		return null;
	}

	@Override
	public Weather getById(Integer id) {
		return null;
	}

	@Override
	public Weather saveOrUpdate(Weather domainObject) {
		return null;
	}

	@Override
	public void delete(Integer id) {

	}


}
