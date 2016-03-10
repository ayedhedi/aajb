package aajb.service.controller;

import aajb.domain.school.Parent;
import aajb.service.ParentService;
import aajb.service.SecurityService;
import aajb.service.SecurityServiceImpl;
import aajb.service.dto.ParentDto;
import aajb.service.validator.ParentValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
    @Autowired
    private ParentValidator parentValidator;
    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Qualifier("securityService")
    @Autowired
    private SecurityService securityService;


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(parentValidator);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public HashMap<String,Object> login(
            @RequestParam String login,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        logger.info("Getting login-in request: ("+login+")");
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));


        UserDetails userDetails = userDetailsService.loadUserByUsername(login);

        if (userDetails == null || !securityService.isMatches(password, userDetails.getPassword())) {
            logger.warn("Cannot login-user: ");
            results.put("status", "false");
            results.put("errors", (new String[] {env.getProperty("api.errorcode.loginOrPasswordIncorrect")}));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return results;
        }


        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean isAdmin = false;
        for(GrantedAuthority grantedAuthority:auth.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        results.put("status", "true");
        results.put("isAdmin", isAdmin);
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public HashMap<String,Object> logout(
            HttpServletRequest request, HttpServletResponse response
    ) {
        HashMap<String, Object> results = new HashMap<>();
        results.put("version", env.getProperty("api.version"));
        results.put("authors", env.getProperty("api.authors"));
        results.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            results.put("status","true");
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            results.put("status","false");
        }


        return results;
    }

}
