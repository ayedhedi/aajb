package aajb.service.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by ayed.h on 26/02/2016.
 */
@Getter
@Setter
@NoArgsConstructor
public class InvalidOperationException extends  ApiException{
    private String message;

    public InvalidOperationException(String errorCode, String message) {
        super(errorCode);
        this.message = message;
    }
}
