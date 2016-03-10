package aajb.service.dto;

import aajb.domain.school.Registration;
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
    private Set<ChequeDto> cheques;
    private Date date;


    public static RegistrationDto asDto(Registration registration) {
        RegistrationDto dto = new RegistrationDto();
        dto.setNum(registration.getNum());
        dto.setState(registration.getState().toString());
        dto.setFirstParent(ParentDto.asParentDto(registration.getFirstParent()));
        if (registration.getSecondParent()!=null) {
            dto.setSecondParent(ParentDto.asParentDto(registration.getSecondParent()));
        }
        dto.setId(registration.getId());

        dto.setStudents(new HashSet<>());
        registration.getStudents().forEach(student -> dto.getStudents().add(StudentDto.asDto(student)));

        dto.setCheques(new HashSet<>());
        registration.getCheques().forEach(check -> dto.getCheques().add(ChequeDto.toCheckDto(check)));

        dto.setDate(registration.getDateTime());
        return dto;
    }
}
