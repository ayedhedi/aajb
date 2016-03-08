package aajb.domain.user;

/**
 * Created by ayed.h on 22/02/2016.
 */

import aajb.domain.school.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="ID")
public class User extends Person{

    @Column(unique=true,nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(unique=true,nullable = false)
    private String email;

    private String state=State.ACTIVE.getState();

    @ElementCollection(targetClass = UserProfileType.class)
    @JoinTable(name = "user_profiles", joinColumns = @JoinColumn(name = "userID"))
    @Enumerated(EnumType.STRING)
    private Set<UserProfileType> userProfiles = new HashSet<>();


}