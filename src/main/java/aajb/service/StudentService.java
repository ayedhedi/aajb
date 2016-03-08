package aajb.service;

import aajb.domain.school.Student;
import aajb.service.exceptions.InvalidDataException;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface StudentService {
    Student createStudent(Student student) throws InvalidDataException, ParseException;
    Student deleteStudent(int id)  throws InvalidDataException;
    boolean isBirthDateValid(Date date);
}
