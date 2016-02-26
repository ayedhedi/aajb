package aajb.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ayed.h on 25/02/2016.
 */
@Getter
@Setter
@AllArgsConstructor
public class AccountLockedException extends ApiException {

    public AccountLockedException(String errorCode) {
        super(errorCode);
    }
}
