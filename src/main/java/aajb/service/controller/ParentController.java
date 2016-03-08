package aajb.service.controller;

import aajb.domain.school.Parent;
import aajb.service.ParentService;
import aajb.service.dto.ParentDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ayed.h on 04/03/2016.
 */
@RestController
@RequestMapping("/api/secure/parent")
public class ParentController {
    private static final Logger logger = Logger.getLogger(ParentController.class.getSimpleName());

    @Qualifier("environment")
    @Autowired
    private Environment environment;
    @Autowired
    private ParentService parentService;


    @RequestMapping(method = RequestMethod.GET,value = "/findByEmail")
    public HashMap<String,Object> findByEmail(
            @RequestParam String email
    ) {
        logger.info("Getting find parent by email request: "+email);
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Parent parent = parentService.findParentByEmail(email);
        if (parent==null) {
            results.put("status","false");
        }else {
            results.put("status","true");
            results.put("parent", ParentDto.asParentDto(parent));
        }

        return results;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/find")
    public List<ParentDto> findParents(
            @RequestParam String match
    ) {
        logger.info("Getting find parent containing: "+match);
        return parentService.findParents(match);
    }
}
