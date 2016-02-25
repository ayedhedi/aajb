package aajb.controller;

import aajb.domain.school.Parent;
import aajb.service.ParentService;
import aajb.service.dto.ParentDto;
import aajb.validator.ParentValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

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
    @Qualifier("parentValidator")
    @Autowired
    private ParentValidator parentValidator;


    @RequestMapping(method = RequestMethod.GET)
    public HashMap<String,Object> login(
            @RequestParam String login,
            @RequestParam String password
    ) {
        logger.info("Getting login-in request: ("+login+", "+password+")");
        return null;
    }

    /**
     * A non secure method used to ask the server to create a new Parent Account.
     * If the information given are correct the account will be create but have to be validated by the ADMIN
     * @param parentDto a Json represents the parent account to create
     * @return a Json of the
     */
    @RequestMapping(method = RequestMethod.POST, value = "/parent")
    public HashMap<String,Object> createParent(
            @RequestBody ParentDto parentDto,
            BindingResult validationResult
    ) {
        logger.info("Got create parent request.");

        //prepare the answer
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Parent parent = parentService.convertFromDto(parentDto);
        //validate the input data
        parentValidator.validate(parent,validationResult);

        //check errors
        if (validationResult.hasErrors()) {
            results.put("status", "false");
            List<String> errors = new ArrayList<>();
            errors.addAll(validationResult.getAllErrors().stream().map(ObjectError::getCode).collect(Collectors.toList()));
            results.put("errors", errors);
            return results;
        }else {
            boolean save = parentService.createParent(parentDto);

            if (save) {
                //the object is now saved, return ok !!
                results.put("status", "true");
                results.put("message", "New Parent is now saved");
                //get parent information
                parentDto =  parentService.convertToDto(parentService.findByLogin(parent.getLogin()));
                //hide the password
                parentDto.setPassword(null);
                results.put("parent",parentDto);
                return results;
            }else {
                //problem where saving the object
                results.put("status", "false");
                results.put("errors", env.getProperty("api.errorcode.internalError"));
                results.put("message", "Cannot save new Parent instance !!");
                return results;
            }
        }
    }


    //public Properties
}
