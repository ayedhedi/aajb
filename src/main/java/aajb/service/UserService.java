package aajb.service;

import aajb.domain.user.User;

/**
 * Created by ayed.h on 22/02/2016.
 */
public interface UserService {
    User findById(int id);
    User findByLogin(String sso);

}
