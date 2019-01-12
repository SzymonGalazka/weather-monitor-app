package sgalazka.springframework.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sgalazka.springframework.domain.Weather;

import java.util.List;
import java.util.Optional;

public interface WeatherService extends CRUDService<Weather> {

	void saveWeather(Weather weather, Integer userId);

	void deleteAllWeatherForUserId(Integer userId);

	Page<Weather> listPaginated(Pageable pageable, Integer userId, Optional<String> city, Optional<String> dates);

	List<Weather> findAllByUserId(Integer userId);

	List<Weather> findAllByCity(String city, Integer userId);

	Weather getWeatherForCity(String city, Integer userId);

	List<String> calculateStats(Page<Weather> weatherPage);
}
