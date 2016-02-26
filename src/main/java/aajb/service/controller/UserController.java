package aajb.service.controller;

import aajb.service.UserService;
import aajb.service.UserServiceImpl;
import aajb.service.exceptions.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by ayed.h on 25/02/2016.
 */
@RestController
@RequestMapping("/api/secure/user")
public class UserController {
    Logger logger = Logger.getLogger(UserController.class.getSimpleName());

    @Autowired
    private Environment env;
    @Qualifier("userService")
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public HashMap<String,Object> activateAccount(
            @RequestParam String code,
            @RequestParam String email
    ) {
        logger.info("Getting activation request: ("+email+")");
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        try {
            logger.info("Account "+email+" is now activated ");
            userService.activateUser(email, code);
            results.put("state","true");
        } catch (ApiException e) {
            logger.info("Cannot activate account: "+e.getErrorCode());
            results.put("state","false");
            results.put("errors", (new String[] {e.getErrorCode()}));
            results.put("message", e.getMessage());
        }

        return results;
    }
}
