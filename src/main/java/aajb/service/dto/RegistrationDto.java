package aajb.service.dto;

import aajb.domain.school.Registration;
import aajb.domain.school.Student;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Data
public class RegistrationDto {

    private Integer id;
    private int num;
    private String state;
    private ParentDto firstParent;
    private ParentDto secondParent;
    private Set<StudentDto> students;
    private Date date;

    public static Registration asRegistration(RegistrationDto dto){
        Registration registration = new Registration();
        registration.setFirstParent(ParentDto.asParent(dto.getFirstParent()));
        registration.setSecondParent(ParentDto.asParent(dto.getSecondParent()));
        registration.setStudents(new HashSet<>());
        for(StudentDto sDto:dto.getStudents()) {
            registration.getStudents().add(StudentDto.asStudent(sDto));
        }
        return registration;
    }

    public static RegistrationDto asDto(Registration registration) {
        RegistrationDto dto = new RegistrationDto();
        dto.setNum(registration.getNum());
        dto.setState(registration.getState().toString());
        dto.setFirstParent(ParentDto.asParentDto(registration.getFirstParent()));
        dto.setSecondParent(ParentDto.asParentDto(registration.getSecondParent()));
        dto.setStudents(new HashSet<>());
        dto.setId(registration.getId());
        for(Student student:registration.getStudents()) {
            dto.getStudents().add(StudentDto.asDto(student));
        }
        dto.setDate(registration.getDateTime());
        return dto;
    }
}
