package aajb.validator;

import aajb.domain.school.Parent;
import aajb.service.ParentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Component
public class ParentValidator implements Validator {
    private static final Logger logger = Logger.getLogger(ParentValidator.class.getSimpleName());

    @Qualifier("environment")
    @Autowired
    private Environment env;
    @Qualifier("parentService")
    @Autowired
    private ParentService parentService;


    @Override
    public boolean supports(Class<?> clazz) {
        return Parent.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Parent parent = (Parent) target;
        //check data
        if (parent.getLogin()==null ||
                !Pattern.compile(env.getProperty("pattern.login"))
                        .matcher(parent.getLogin()).matches()) {
            logger.info("Invalid Login name:"+parent.getLogin());
            errors.reject(env.getProperty("api.errorcode.invalidLogin"));
        }

        if (parent.getPassword()==null ||
                !Pattern.compile(env.getProperty("pattern.password"))
                        .matcher(parent.getPassword()).matches()) {
            logger.info("Invalid Password:"+parent.getPassword());
            errors.reject(env.getProperty("api.errorcode.invalidPassword"));
        }

        if (parent.getFirstName()==null||
                !Pattern.compile(env.getProperty("pattern.firstName"))
                        .matcher(parent.getFirstName()).matches()) {
            logger.info("Invalid First Name:"+parent.getFirstName());
            errors.reject(env.getProperty("api.errorcode.invalidFirstName"));
        }

        if (parent.getLastName()==null||
                !Pattern.compile(env.getProperty("pattern.lastName"))
                        .matcher(parent.getLastName()).matches()) {
            logger.info("Invalid Last Name:"+parent.getLastName());
            errors.reject(env.getProperty("api.errorcode.invalidLastName"));
        }

        if (parent.getEmail()==null ||
                !Pattern.compile(env.getProperty("pattern.email"))
                        .matcher(parent.getEmail()).matches()) {
            logger.info("Invalid Email:"+parent.getEmail());
            errors.reject(env.getProperty("api.errorcode.invalidEmail"));
        }

        //check if the email exists
        if (parent.getEmail()!=null && parentService.isEmailUsed(parent.getEmail())) {
            logger.info("The email:"+parent.getEmail()+" is already used ");
            errors.reject(env.getProperty("api.errorcode.emailAlreadyInUse"));
        }

        //check if the login exists
        if (parent.getLogin()!=null && parentService.isLoginUser(parent.getLogin())) {
            logger.info("The Login:"+parent.getLogin()+" is already in use");
            errors.reject(env.getProperty("api.errorcode.loginAlreadyInUse"));
        }
    }
}
