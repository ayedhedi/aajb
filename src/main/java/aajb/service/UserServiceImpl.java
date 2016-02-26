package aajb.service;

import aajb.domain.user.State;
import aajb.domain.user.User;
import aajb.domain.user.UserProfileType;
import aajb.repository.UserRepository;
import aajb.service.dto.UserDto;
import aajb.service.exceptions.AccountLockedException;
import aajb.service.exceptions.InvalidDataException;
import aajb.service.exceptions.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Qualifier("securityService")
    @Autowired
    private SecurityService securityService;

    @Qualifier("environment")
    @Autowired
    private Environment environment;


    @Override
    public UserDto login(String login, String password) throws InvalidDataException, AccountLockedException {
        User user = userRepository.findByLogin(login);

        if (user == null || !securityService.isMatches(password,user.getPassword())) {
            throw new InvalidDataException(environment.getProperty("api.errorcode.loginOrPasswordIncorrect")
                    , "Login or password Incorrect");
        }

        if (!user.getState().equals(State.ACTIVE.getState())) {
            throw new AccountLockedException(environment.getProperty("api.errorcode.accountLocked"));
        }


        UserDto userDto = UserDto.asUserDto(user);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void activateUser(String email, String code) throws InvalidDataException, InvalidOperationException {

        //find the account
        User account = userRepository.findByEmail(email);
        if (account == null) {
            throw  new InvalidDataException(environment.getProperty("api.errorcode.emailIncorrect"),
                    "Cannot find email: "+email);
        }

        //checks the user
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!canModifyAccount(userDetails,account)) {
            throw new InvalidOperationException(environment.getProperty("api.errorcode.userCannotModifyAccount"),"" +
                    "the current user "+email+" cannot modify the account !");
        }

        //checks the account state
        if (!State.LOCKED.getState().equals(account.getState())) {
            throw new InvalidOperationException(environment.getProperty("api.errorcode.accountNotLocked"),"" +
                    "Cannot activate an unlocked account!: "+email);
        }

        //checks the code
        if (!code.equals(account.getActivationCode())){
            throw new InvalidOperationException(environment.getProperty("api.errorcode.incorrectActivationCode"),"" +
                    "Cannot activate account!: "+email+" incorrect code");
        }

        //modify the account
        account.setState(State.ACTIVE.getState());
    }

    private boolean canModifyAccount(UserDetails userDetails, User account) {
        return (userDetails.getUsername().equals(account.getLogin())) ||
                userDetails.getAuthorities().stream().filter(grantedAuthority ->
                grantedAuthority.getAuthority().equals
                        ("ROLE_"+ UserProfileType.DBA.getUserProfileType())).findAny().orElse(null) != null;
    }

}