package aajb.service.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by ayed.h on 25/02/2016.
 */
@Getter
@Setter
@NoArgsConstructor
public class InvalidDataException extends ApiException {
    private String message;

    public InvalidDataException(String errorCode, String message) {
        super(errorCode);
        this.message = message;
    }
}
