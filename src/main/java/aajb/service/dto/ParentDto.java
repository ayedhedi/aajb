package aajb.service.dto;

import aajb.domain.school.Gender;
import aajb.domain.school.Parent;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Data
public class ParentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String login;
    private String password;
    private String email;
    private String tel;
    private String telGsm;
    private String telPro;
    private long ssn;     //social security number
    private int caf;

    public static Parent asParent(ParentDto dto) {
        Parent parent = new Parent();
        parent.setFirstName(dto.firstName);
        parent.setLastName(dto.lastName);
        parent.setGender(Gender.getGender(dto.gender));
        parent.setLogin(dto.login);
        parent.setPassword(dto.password);
        parent.setEmail(dto.email);
        parent.setTel(dto.tel);
        parent.setTelGsm(dto.telGsm);
        parent.setTelPro(dto.telPro);
        parent.setSsn(dto.ssn);
        parent.setCaf(dto.caf);
        return parent;
    }

    public static ParentDto asParentDto(Parent parent) {
        ParentDto dto = new ParentDto();
        dto.id = String.valueOf(parent.getId());
        dto.firstName = parent.getFirstName();
        dto.lastName = parent.getLastName();
        dto.gender=parent.getGender().toString();
        dto.login =parent.getLogin();
        dto.password=parent.getPassword();
        dto.email=parent.getEmail();
        dto.tel=parent.getTel();
        dto.telGsm = parent.getTelGsm();
        dto.telPro = parent.getTelPro();
        dto.ssn=parent.getSsn();
        dto.caf=parent.getCaf();
        return dto;
    }
}
