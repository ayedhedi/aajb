package aajb.service;

import aajb.dao.repository.UserRepository;
import aajb.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ayed.h on 22/02/2016.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    public User findById(int id) {
        return userRepository.findOne(id);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

}