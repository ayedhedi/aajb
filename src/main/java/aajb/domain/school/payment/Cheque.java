package aajb.domain.school.payment;

import aajb.domain.school.Registration;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Created by ayed.h on 10/03/2016.
 */
@Entity
@Data
@NoArgsConstructor
public class Cheque {

    @Id
    @GeneratedValue
    private Integer id;
    @Min(value = 1)
    private int amount;
    private String bankName;
    private String number;

    /**
     * if equals to false then adjustableDate and adjustedDate will be null
     */
    private boolean adjustable;

    /**
     * date where the check could be adjusted
     */
    @Temporal(TemporalType.DATE)
    private Date adjustableDate;

    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private Registration registration;
}
