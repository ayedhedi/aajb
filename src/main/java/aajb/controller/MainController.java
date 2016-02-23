package aajb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * Created by ayed.h on 22/02/2016.
 */
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    Environment env;

    @RequestMapping(value = "/version")
    public Properties printProperty() {
        Properties properties = new Properties();
        properties.setProperty("version", env.getProperty("api.version"));
        return properties;
    }
}
