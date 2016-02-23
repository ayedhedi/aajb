package aajb.controller;

import aajb.domain.school.Gender;
import aajb.domain.school.Parent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ayed.h on 22/02/2016.
 */
@RestController
@RequestMapping("/api/secure/parents")
public class ParentController {


    @RequestMapping(method = RequestMethod.GET, value = "/")
    public Parent getParent() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/static")
    public Parent getStatic() {
        Parent p = new Parent();
        p.setFirstName("static_fistName");
        p.setLastName("static_lastName");
        p.setId(124556);
        p.setGender(Gender.MALE);
        p.setCaf(352314);
        p.setEmail("a.b@g.com");
        p.setSsn(2522454l);
        p.setTel("36987560125");
        p.setTelGsm("32623621");
        p.setTelPro("54424222");
        return p;
    }
}
