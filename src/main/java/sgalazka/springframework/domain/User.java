package sgalazka.springframework.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jt on 12/14/15.
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private Integer id;

	private String username;

	@Transient
	private String password;

	private String encryptedPassword;
	private Boolean enabled = true;

	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Weather> weathers = new ArrayList<>();


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable
	private List<Role> roles = new ArrayList<>();

	public User() {
	}

	public User(String username, String password, String encryptedPassword, Boolean enabled, List<Weather> weathers, List<Role> roles) {
		this.username = username;
		this.password = password;
		this.encryptedPassword = encryptedPassword;
		this.enabled = enabled;
		this.weathers = weathers;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (!this.roles.contains(role)) {
			this.roles.add(role);
		}

		if (!role.getUsers().contains(this)) {
			role.getUsers().add(this);
		}
	}

	public void removeRole(Role role) {
		this.roles.remove(role);
		role.getUsers().remove(this);
	}

	public List<Weather> getWeathers() {
		return weathers;
	}

	public void setWeathers(List<Weather> weathers) {
		this.weathers = weathers;
	}
}
