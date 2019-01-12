package sgalazka.springframework.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by jt on 12/16/15.
 */
@MappedSuperclass
public class AbstractDomainClass implements DomainObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@Version
	private Integer version;

	private LocalDate dateCreated;
	private LocalDate lastUpdated;

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
		lastUpdated = LocalDate.now();
		if (dateCreated == null) {
			dateCreated = LocalDate.now();
		}
	}
}
