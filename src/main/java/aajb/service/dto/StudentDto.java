package aajb.service.dto;

import aajb.domain.school.Gender;
import aajb.domain.school.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
   private Integer id;
    private String firstName;
    private String lastName;
    private String gender;
    private String className;
    private Date   birthDate;
    private String remarks;


    public static Student asStudent(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.id);
        student.setFirstName(dto.firstName);
        student.setLastName(dto.lastName);
        student.setGender(Gender.getGender(dto.getGender()));
        student.setClassName(dto.className);
        student.setBirthDate(dto.birthDate);
        student.setRemarks(dto.remarks);
        return student;
    }

    public static StudentDto asDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.id = student.getId();
        dto.firstName = student.getFirstName();
        dto.lastName = student.getLastName();
        dto.gender = student.getGender().toString();
        dto.className = student.getClassName();
        dto.birthDate = student.getBirthDate();
        dto.remarks = student.getRemarks();
        return dto;
    }
}
