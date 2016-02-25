package aajb.service;

import aajb.domain.school.Parent;
import aajb.service.dto.ParentDto;

/**
 * Created by ayed.h on 24/02/2016.
 */
public interface ParentService {

    ParentDto convertToDto(Parent parent);

    Parent convertFromDto(ParentDto parentDto);

    boolean createParent(ParentDto parentDto);

    boolean isEmailUsed(String email);

    boolean isLoginUser(String login);

    Parent findByLogin(String login);
}
