package sgalazka.springframework.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sgalazka.springframework.domain.User;
import sgalazka.springframework.domain.Weather;

import java.util.List;


public interface WeatherRepository extends CrudRepository<Weather, Integer> {

	List<Weather> findAllByUserId(Integer userId);

}
