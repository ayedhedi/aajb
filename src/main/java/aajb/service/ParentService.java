package aajb.service;

import aajb.service.dto.ParentDto;
import aajb.service.exceptions.InvalidDataException;

/**
 * Created by ayed.h on 24/02/2016.
 */
public interface ParentService {

    ParentDto createParent(ParentDto parentDto) throws InvalidDataException;
    boolean isEmailUsed(String email);
    boolean isLoginUser(String login);
}
