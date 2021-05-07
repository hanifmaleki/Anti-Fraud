package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Incomplete input data")
public class IncompletDataException extends RuntimeException{
    public IncompletDataException(String message) {
        super(message);
    }
}
