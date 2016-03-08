package aajb.service;

import aajb.domain.school.Student;
import aajb.repository.StudentRepository;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Service
public class StudentServiceImpl implements StudentService {

    private static Logger logger = Logger.getLogger(StudentService.class.getSimpleName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

    @Qualifier("studentRepository")
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private Environment env;


    @Override
    public Student createStudent(Student student) throws InvalidDataException, ParseException {

        logger.info("Creating new student ... "+student.getFirstName());

        if (student.getFirstName()==null || !Pattern.compile(env.getProperty("pattern.firstName"))
                .matcher(student.getFirstName()).matches()) {
            logger.warn("Incorrect first name: "+student.getFirstName());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidFirstName"),"Invalid First Name");
        }

        if (student.getLastName()==null || !Pattern.compile(env.getProperty("pattern.lastName"))
                .matcher(student.getLastName()).matches()) {
            logger.warn("Incorrect last name: "+student.getLastName());
            throw new InvalidDataException(env.getProperty("api.errorcode.firstParentNull"),"Invalid Last Name");
        }

        if (student.getFirstParent()==null) {
            logger.warn("Incorrect First Parent !!");
            throw new InvalidDataException(env.getProperty("api.errorcode.firstParentNull"),"First Parent Cannot be null");
        }

        if (student.getSecondParent()!=null && Objects.equals(student.getSecondParent().getId(),
                student.getFirstParent().getId())) {
            logger.warn("First Parent & second parent are the same !!");
            throw new InvalidDataException(env.getProperty("api.errorcode.firstParentEqualsSecondParent"),"First & second Parents are the same ");
        }

        if (!isBirthDateValid(student.getBirthDate())) {
            throw new InvalidDataException(env.getProperty("api.errorcode.incorrectDate"),"Birth date is invalid");
        }

        //set active
        student.setActive(true);

        student = studentRepository.save(student);

        if (student!=null) {
            logger.info("New student has been created and saved id="+student.getId());
            return student;
        }else {
            logger.warn("Cannot save Object to student database !!");
            throw new InvalidDataException(env.getProperty("api.errorcode.internalError"),"Internal Error");
        }
    }

    @Override
    public Student deleteStudent(int id) throws InvalidDataException {
        logger.info("Deleting student with id: "+id);

        Student student = studentRepository.findOne(id);
        if (student == null ) {
            logger.warn("Cannot delete student, incorrect id");
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidPersonId"),
                    "Cannot delete student, incorrect id: "+id);
        }

        student.setActive(false);
        student = studentRepository.save(student);

        return  student;
    }

    @Override
    public boolean isBirthDateValid(Date date) {
        if (date == null) {
            return false;
        }

        try {
            return (
                    date.getTime()==sdf.parse(env.getProperty("date.min")).getTime() ||
                    date.getTime()==sdf.parse(env.getProperty("date.max")).getTime() ||
                    (date.after(sdf.parse(env.getProperty("date.min"))) &&
                    date.before(sdf.parse(env.getProperty("date.max"))))
                    );
        } catch (ParseException e) {
            return false;
        }
    }

}
