package aajb.service.controller;

import aajb.domain.school.Parent;
import aajb.service.ParentService;
import aajb.service.dto.ParentDto;
import aajb.service.exceptions.ApiException;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.GET)
    public HashMap<String, Object> getParents(){
        logger.info("Getting read parents request");
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        List<ParentDto> parentDtos = parentService.readParents();
        results.put("status","true");
        results.put("parents",parentDtos);
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/paged")
    public HashMap<String, Object> getParentsPage(
            @RequestParam int page,
            @RequestParam int size
    ){
        logger.info("Getting read parents request (pagination)");
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        try {
            int numberOfPage = parentService.getNumberOfPage(size);
            List<ParentDto> parentDtos = parentService.readParents(page, size);
            int nextPage=(page==numberOfPage?0:page+1);
            results.put("page",page);
            results.put("next",nextPage);
            results.put("numberOfPages",numberOfPage);
            results.put("parents",parentDtos);

        } catch (InvalidDataException e) {
            results.put("status", "false");
            logger.warn("Error: "+e.getMessage());
        }

        return results;
    }

    @RequestMapping(method = RequestMethod.POST)
    public HashMap<String, Object> createParent(
            @RequestBody ParentDto parentDto
    ) {
        logger.info("Getting create parent request: "+parentDto);
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", environment.getProperty("api.version"));
        results.put("authors", environment.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        try {
            parentDto = parentService.createParent(parentDto);
            logger.info("New parent has been save with id: "+parentDto.getId());
            results.put("status", "true");
            results.put("parent",parentDto);
        } catch (ApiException e) {
            logger.warn("Cannot create parent Error:"+e.getMessage());
            results.put("status", "false");
            results.put("errors", (new String[] {e.getErrorCode()}));
            results.put("message", e.getMessage());
        }

        return results;
    }

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
