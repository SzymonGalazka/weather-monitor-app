package sgalazka.springframework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Weather extends AbstractDomainClass {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WEATHER_ID")
	private Integer id;
	private String city;
	private String main;
	private String description;
	private double temp;
	private double tempMin;
	private double tempMax;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Weather() {
	}

	public Weather(String city, String main, String description, double temp, double tempMin, double tempMax, User user) {
		this.city = city;
		this.main = main;
		this.description = description;
		this.temp = temp;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.user = user;
	}

	public Weather(String city, String main, String description, double temp, double tempMin, double tempMax) {
		this.city = city;
		this.main = main;
		this.description = description;
		this.temp = temp;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
