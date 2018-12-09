package sgalazka.springframework.common.pojo;

/**
 * Created by Piotrek on 06.12.2018.
 */
public class UserCreds {

	private Integer id;

	private String username;

	private String password;

	private String passwordRepeat;

	public UserCreds() {
	}

	public UserCreds(Integer id, String username, String password, String passwordRepeat) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}
}
