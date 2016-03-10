package aajb.service.dto;

import aajb.domain.school.Gender;
import aajb.domain.school.Parent;
import aajb.domain.school.Registration;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Data
public class ParentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String tel;
    private String telGsm;
    private String telPro;
    private String ssn;     //social security number
    private int caf;
    private String address;
    private String job;
    private List<String> registrations;

    public static Parent asParent(ParentDto dto) {
        Parent parent = new Parent();
        parent.setId(dto.id);
        parent.setFirstName(dto.firstName);
        parent.setLastName(dto.lastName);
        parent.setGender(Gender.getGender(dto.gender));
        parent.setEmail(dto.email);
        parent.setTel(dto.tel);
        parent.setTelGsm(dto.telGsm);
        parent.setTelPro(dto.telPro);
        parent.setSsn(dto.ssn);
        parent.setCaf(dto.caf);
        parent.setJob(dto.job);
        if (dto.address != null) {
            parent.setAddress(dto.address);
        }
        return parent;
    }

    public static ParentDto asParentDto(Parent parent) {
        ParentDto dto = new ParentDto();
        dto.id = parent.getId();
        dto.firstName = parent.getFirstName();
        dto.lastName = parent.getLastName();
        dto.gender=parent.getGender().toString();
        dto.email=parent.getEmail();
        dto.tel=parent.getTel();
        dto.telGsm = parent.getTelGsm();
        dto.telPro = parent.getTelPro();
        dto.ssn=parent.getSsn();
        dto.caf=parent.getCaf();
        dto.job=parent.getJob();
        if (parent.getAddress() != null) {
            dto.address = parent.getAddress();
        }
        if (parent.getRegistrations() != null) {
            dto.registrations = new ArrayList<>();
            parent.getRegistrations().forEach(registration ->
                dto.registrations.add(String.valueOf(registration.getId()))
            );
        }
        return dto;
    }
}
