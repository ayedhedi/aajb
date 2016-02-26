package aajb.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by ayed.h on 25/02/2016.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiException extends Exception {
    private String errorCode;
}
