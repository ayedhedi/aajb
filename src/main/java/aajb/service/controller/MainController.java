package aajb.service.controller;

import aajb.service.ParentService;
import aajb.service.UserService;
import aajb.service.dto.ParentDto;
import aajb.service.dto.UserDto;
import aajb.service.exceptions.AccountLockedException;
import aajb.service.exceptions.ApiException;
import aajb.service.exceptions.InvalidDataException;
import aajb.service.validator.ParentValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ayed.h on 22/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MainController {
    Logger logger = Logger.getLogger(MainController.class.getSimpleName());

    @Autowired
    private Environment env;
    @Autowired
    private ParentService parentService;
    @Qualifier("userService")
    @Autowired
    private UserService userService;
    @Autowired
    private ParentValidator parentValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(parentValidator);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public HashMap<String,Object> login(
            @RequestParam String login,
            @RequestParam String password
    ) {
        logger.info("Getting login-in request: ("+login+")");
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));


        UserDto userDto;

        try {
            userDto = userService.login(login,password);
        } catch (ApiException e) {
            logger.warn("Cannot create parent: "+e.getMessage());
            results.put("status", "false");
            results.put("errors", (new String[] {e.getErrorCode()}));
            results.put("message", e.getMessage());
            return results;
        }



        if (userDto == null) {
            logger.info("login-in "+login+" failed !!");
            results.put("status","false");
            results.put("errors",(new String[] {env.getProperty("api.errorcode.loginOrPasswordIncorrect")}));
        }else {
            logger.info("Success login-in "+login+" !");
            results.put("status","true");
            results.put("user", userDto);
        }

        return results;
    }

    /**
     * A non secure method used to ask the server to create a new Parent Account.
     * If the information given are correct the account will be create but have to be validated by the ADMIN
     * @param parentDto a Json represents the parent account to create
     * @return a Json of the
     */
    @RequestMapping(method = RequestMethod.POST, value = "/parent")
    public HashMap<String,Object> createParent(
            @RequestBody @Valid ParentDto parentDto,
            BindingResult validationResult
    ) {
        logger.info("Got create parent request.");

        //prepare the answer
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        //check errors
        if (validationResult.hasErrors()) {
            results.put("status", "false");
            List<String> errors = new ArrayList<>();
            errors.addAll(validationResult.getAllErrors().stream().map(ObjectError::getCode).collect(Collectors.toList()));
            results.put("errors", errors);
            return results;
        }else {
            try {
                parentDto = parentService.createParent(parentDto);
                results.put("status", "true");
                results.put("message", "New Parent is now saved");
                results.put("parent",parentDto);
                return results;
            } catch (InvalidDataException e) {
                logger.warn("Cannot create parent: "+e.getMessage());
                results.put("status", "false");
                results.put("errors", e.getErrorCode());
                results.put("message", e.getMessage());
                return results;
            }
        }
    }


    //public Properties
}
