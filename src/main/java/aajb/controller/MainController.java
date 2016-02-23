package aajb.controller;

import aajb.dao.repository.ParentRepository;
import aajb.domain.school.Parent;
import aajb.domain.user.State;
import aajb.domain.user.UserProfileType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by ayed.h on 22/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MainController {
    Logger logger = Logger.getLogger(MainController.class.getSimpleName());

    @Autowired
    Environment env;
    @Autowired
    private ParentRepository parentRepository;


    @RequestMapping(value = "/version")
    public Properties printProperty() {
        Properties properties = new Properties();
        properties.setProperty("version", env.getProperty("api.version"));
        return properties;
    }


    /**
     * A non secure method used to ask the server to create a new Parent Account.
     * If the information given are correct the account will be create but have to be validated by the ADMIN
     * @param parent a Json represents the parent account to create
     * @return a Json of the
     */
    @RequestMapping(method = RequestMethod.POST, value = "/parent")
    public HashMap<String,Object> createParent(
            @RequestBody Parent parent
    ) {
        //prepare the result
        HashMap<String,Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        boolean status = true;
        StringBuilder errors = new StringBuilder();

        //check data
        if (parent.getLogin()==null ||
                !Pattern.compile(env.getProperty("pattern.login"))
                        .matcher(parent.getLogin()).matches()) {
            logger.info("Invalid Login name:"+parent.getLogin());
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.invalidLogin"));
        }

        if (parent.getPassword()==null ||
                !Pattern.compile(env.getProperty("pattern.password"))
                        .matcher(parent.getPassword()).matches()) {
            logger.info("Invalid Password:"+parent.getPassword());
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.invalidPassword"));
        }

        if (parent.getFirstName()==null||
                !Pattern.compile(env.getProperty("pattern.firstName"))
                        .matcher(parent.getFirstName()).matches()) {
            logger.info("Invalid First Name:"+parent.getFirstName());
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.invalidFirstName"));
        }

        if (parent.getLastName()==null||
                !Pattern.compile(env.getProperty("pattern.lastName"))
                        .matcher(parent.getLastName()).matches()) {
            logger.info("Invalid Last Name:"+parent.getLastName());
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.invalidLastName"));
        }

        if (parent.getEmail()==null ||
                !Pattern.compile(env.getProperty("pattern.email"))
                    .matcher(parent.getEmail()).matches()) {
            logger.info("Invalid Email:"+parent.getEmail());
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.invalidEmail"));
        }

        //check if the email exists
        if (parent.getEmail()!=null && parentRepository.findByEmail(parent.getEmail())!=null) {
            logger.info("The email:"+parent.getEmail()+" is already used ");
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.emailAlreadyInUse"));
        }

        //check if the login exists
        if (parent.getLogin()!=null && parentRepository.findByLogin(parent.getLogin())!=null) {
            logger.info("The Login:"+parent.getLogin()+" is already in use");
            status = false;
            errors.append(",").append(env.getProperty("api.errorcode.loginAlreadyInUse"));
        }


        if (!status) {
            results.put("status","false");
            results.put("errors",errors.toString().substring(1).split(","));
            return results;
        }else {
            //No need of the Id
            parent.setId(null);
            //set account locked ---> to be unlocked by the manager
            parent.setState(State.LOCKED.getState());
            //set user profile USER
            parent.setUserProfiles(new HashSet<>());
            parent.getUserProfiles().add(UserProfileType.USER);

            parent = parentRepository.save(parent);
            if (parent==null) {
                results.put("status","false");
                results.put("errors",env.getProperty("api.errorcode.internalError"));
                results.put("message","Cannot save new Parent instance !!");
                return  results;
            }else {
                results.put("status","true");
                results.put("parent",parent);
                return results;
            }
        }
    }
}
