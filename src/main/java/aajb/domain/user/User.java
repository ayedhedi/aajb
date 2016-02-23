package aajb.domain.user;

/**
 * Created by ayed.h on 22/02/2016.
 */

import aajb.domain.school.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
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
    private String email;

    @Column(nullable=false)
    private String state=State.ACTIVE.getState();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = { @JoinColumn(name = "USER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
    private Set<UserProfile> userProfiles = new HashSet<>();


}