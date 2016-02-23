package aajb.domain.user;

/**
 * Created by ayed.h on 22/02/2016.
 */

import aajb.domain.school.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value="US")
public class User extends Person{

    @Column(unique=true, nullable=false)
    private String login;

    @Column(nullable=false)
    private String password;

    @Email
    @Column(nullable=false)
    @XmlElement(required = true)
    private String email;

    @Column(nullable=false)
    private String state=State.ACTIVE.getState();

    @ElementCollection(targetClass = UserProfileType.class)
    @JoinTable(name = "user_profiles", joinColumns = @JoinColumn(name = "userID"))
    @Enumerated(EnumType.STRING)
    private Set<UserProfileType> userProfiles = new HashSet<>();


}