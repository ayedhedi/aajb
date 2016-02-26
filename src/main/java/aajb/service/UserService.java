package aajb.service;

import aajb.service.dto.UserDto;
import aajb.service.exceptions.AccountLockedException;
import aajb.service.exceptions.InvalidDataException;
import aajb.service.exceptions.InvalidOperationException;

/**
 * Created by ayed.h on 22/02/2016.
 */
public interface UserService {
    UserDto login(String login,String password) throws InvalidDataException, AccountLockedException;
    void activateUser(String email, String code) throws InvalidDataException, InvalidOperationException;
}
