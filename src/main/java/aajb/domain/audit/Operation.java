package aajb.domain.audit;

import aajb.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id @GeneratedValue
    private long id;
    private OperationType type;
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date time;
    @OneToOne
    private User user;
    private String content;
}
