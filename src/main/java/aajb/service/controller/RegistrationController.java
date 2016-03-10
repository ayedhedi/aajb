package aajb.service.controller;

import aajb.service.RegistrationService;
import aajb.service.dto.RegistrationDto;
import aajb.service.exceptions.ApiException;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by ayed.h on 02/03/2016.
 */
@RestController
@RequestMapping(value = "/api/secure/registration")
public class RegistrationController {
    private static final Logger logger = Logger.getLogger(RegistrationController.class.getSimpleName());

    @Qualifier("registrationServiceImpl")
    @Autowired
    private RegistrationService registrationService;
    @Qualifier("environment")
    @Autowired
    private Environment environment;


    @RequestMapping(method = RequestMethod.POST)
    public HashMap<String,Object> create(
            @RequestBody RegistrationDto registrationDto,
            HttpServletResponse response
    ) {
        logger.info("Getting registration request: "+registrationDto);
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        try {
            registrationDto = registrationService.createRegistration(registrationDto);
            logger.info("New registration has been save with id: "+registrationDto.getId());
            results.put("status", "true");
            results.put("registration",registrationDto);
        } catch (ApiException e) {
            logger.warn("Cannot create registration Error:"+e.getMessage());
            results.put("status", "false");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            results.put("errors", (new String[] {e.getErrorCode()}));
            results.put("message", e.getMessage());
        }

        return results;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public HashMap<String,Object> delete(
            @RequestParam int id,
            HttpServletResponse response) {
        logger.info("Delete registration request: "+id);
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        try {
            registrationService.deleteRegistration(id);
            logger.info("Registration "+id+" is now deleted");
            results.put("status", "true");
        } catch (InvalidDataException e) {
            logger.warn("Cannot delete registration "+id+" error:"+e.getMessage());
            results.put("status", "false");
            results.put("errors", (new String[] {e.getErrorCode()}));
            results.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return results;
    }
}
