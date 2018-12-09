package sgalazka.springframework.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Piotrek on 06.12.2018.
 */

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class UserAlreadyInUseException extends RuntimeException {

	public UserAlreadyInUseException(String message) {
		super(message);
	}
}
