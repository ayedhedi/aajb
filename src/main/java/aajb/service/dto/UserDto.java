package aajb.service.dto;

import aajb.domain.school.Gender;
import aajb.domain.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ayed.h on 25/02/2016.
 */
@Getter
@Setter
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String login;
    private String password;
    private String email;



    public static User asUser(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setGender(Gender.getGender(dto.gender));
        user.setLogin(dto.login);
        user.setPassword(dto.password);
        user.setEmail(dto.email);

        return user;
    }

    public static UserDto asUserDto(User user) {
        UserDto dto = new UserDto();
        dto.id = String.valueOf(user.getId());
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.gender= user.getGender().toString();
        dto.login = user.getLogin();
        dto.password= user.getPassword();
        dto.email= user.getEmail();
        return dto;
    }
}
