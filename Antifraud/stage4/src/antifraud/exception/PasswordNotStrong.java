package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Password  is too weak")
public class PasswordNotStrong extends RuntimeException {
    public PasswordNotStrong(int length) {
        super(String.format("Password with length %d is not secured enough.", length));
    }
}
