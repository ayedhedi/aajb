package aajb.service.controller;

import aajb.domain.school.ClassName;
import aajb.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ayed.h on 14/03/2016.
 */
@RestController
@RequestMapping("/api/secure/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * get the number of student for each class name. E.X
     *  {
     *     GA: 12, PA: 45
     *  }
     * @return Json of a map where key is the class name and the value represents the number of student
     */
    @RequestMapping(value = "/classNames", method = RequestMethod.GET)
    public HashMap<String, Object> getListClassNames() {
        HashMap<String, Object> result = new HashMap<>();
        //init the all with 0
        for(ClassName className:ClassName.values()) {
            result.put(className.name(),0);
        }
        //read from data base now
        List<Object[]> studentByClass = studentRepository.findNumberOfStudentByClass();
        for(Object[] tab:studentByClass) {
            result.put(tab[0].toString(),Integer.parseInt(tab[1].toString()));
        }

        result.put("all", ClassName.values());
        return result;
    }
}
