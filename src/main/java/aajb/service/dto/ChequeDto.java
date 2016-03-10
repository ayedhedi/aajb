package aajb.service.dto;

import aajb.domain.school.payment.Cheque;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by ayed.h on 10/03/2016.
 */
@Data
@NoArgsConstructor
public class ChequeDto {

    private Integer id;
    private int amount;
    private String bankName;
    private String number;
    private boolean adjust;
    private Date adjustableDate;
    private Date adjustedDate;
    private String remarks;
    private int registration;

    public static ChequeDto toCheckDto(Cheque cheque) {
        ChequeDto chequeDto = new ChequeDto();
        chequeDto.id = cheque.getId();
        chequeDto.amount = cheque.getAmount();
        chequeDto.bankName = cheque.getBankName();
        chequeDto.number = cheque.getNumber();
        chequeDto.adjust = cheque.isAdjust();
        chequeDto.adjustedDate = cheque.getAdjustedDate();
        chequeDto.adjustableDate = cheque.getAdjustableDate();
        chequeDto.remarks = cheque.getRemarks();
        chequeDto.registration = cheque.getRegistration().getId();
        return chequeDto;
    }

    public static Cheque toCheck(ChequeDto chequeDto) {
        Cheque cheque = new Cheque();
        cheque.setAmount(chequeDto.getAmount());
        cheque.setBankName(chequeDto.getBankName());
        cheque.setNumber(chequeDto.getNumber());
        cheque.setAdjust(chequeDto.isAdjust());
        cheque.setAdjustableDate(chequeDto.getAdjustableDate());
        cheque.setAdjustedDate(chequeDto.getAdjustedDate());
        cheque.setRemarks(chequeDto.getRemarks());
        return cheque;
    }
}
