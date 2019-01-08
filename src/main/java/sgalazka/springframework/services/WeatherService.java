package sgalazka.springframework.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sgalazka.springframework.domain.Weather;

import java.util.List;

public interface WeatherService extends CRUDService<Weather> {

	void saveWeather(Weather weather, Integer userId);

	void deleteAllWeatherForUserId(Integer userId);

	Page<Weather> listPaginated(Pageable pageable, Integer userId);

	List<Weather> findAllByUserId(Integer userId);

	Weather getWeatherForCity(String city, Integer userId);
}
