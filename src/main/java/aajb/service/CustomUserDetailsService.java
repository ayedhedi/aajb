package aajb.service;

/**
 * Created by ayed.h on 22/02/2016.
 */
import java.util.ArrayList;
import java.util.List;

import aajb.repository.UserRepository;
import aajb.domain.user.User;
import aajb.domain.user.UserProfileType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{
    private static  final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getSimpleName());

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        logger.info("User : " + user);
        if(user==null){
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(UserProfileType userProfile : user.getUserProfiles()){
            logger.info("UserProfileType : " + userProfile);
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getUserProfileType()));
        }
        logger.info("authorities :"+authorities);
        return authorities;
    }

}