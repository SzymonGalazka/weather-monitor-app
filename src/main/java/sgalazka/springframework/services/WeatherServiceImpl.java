package sgalazka.springframework.services;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;
import sgalazka.springframework.repositories.UserRepository;
import sgalazka.springframework.repositories.WeatherRepository;

import java.util.List;
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
	public void saveWeather(Weather weather, Integer userId) {
		User user = userRepository.findById(userId);
//		.orElseThrow(() -> new EntityNotFoundException("UserID not found: " + username))
		weather.setUser(user);
		weatherRepository.save(weather);
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
