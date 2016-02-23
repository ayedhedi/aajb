package aajb.domain.user;

/**
 * Created by ayed.h on 22/02/2016.
 */

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
public class UserProfile {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="TYPE", length=15, unique=true, nullable=false)
    private String type = UserProfileType.USER.getUserProfileType();


}