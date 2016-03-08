package aajb.domain.school;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "state!='deleted'")
public class Registration {
    @Id @GeneratedValue
    private Integer id;

    @GeneratedValue
    private int num;

    @Enumerated(EnumType.STRING)
    private RegistrationState state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Parent firstParent;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Parent secondParent;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Student> students;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
}
