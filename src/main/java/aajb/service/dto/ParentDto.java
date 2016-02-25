package aajb.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown =false)
public class ParentDto {
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
}
