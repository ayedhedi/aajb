package aajb.service.validator;

import aajb.service.ParentService;
import aajb.service.dto.ParentDto;
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
    @Autowired
    private ParentService parentService;


    @Override
    public boolean supports(Class<?> clazz) {
        return ParentDto.class.isAssignableFrom(clazz);
    }
    @Override

    public void validate(Object target, Errors errors) {
        ParentDto parent = (ParentDto) target;
        //check data

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
        if (parent.getEmail()!=null && parentService.findParentByEmail(parent.getEmail())!=null) {
            logger.info("The email:"+parent.getEmail()+" is already used ");
            errors.reject(env.getProperty("api.errorcode.emailAlreadyInUse"));
        }

    }
}
