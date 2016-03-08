package aajb.service.controller;

import aajb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by ayed.h on 07/03/2016.
 */
@RestController
@RequestMapping("/api")
public class DataController {


    @Qualifier("studentServiceImpl")
    @Autowired
    private StudentService studentService;

    @RequestMapping(method = RequestMethod.GET, value = "/dataCheck/studentBirthDate")
    public @ResponseBody boolean checkStudentBirthDate(@RequestParam String date) {
        try {
            return studentService.isBirthDateValid(new Date(Long.parseLong(date)));
        }catch (Exception e){
            return false;
        }
    }
}
