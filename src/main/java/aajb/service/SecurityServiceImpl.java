package aajb.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
