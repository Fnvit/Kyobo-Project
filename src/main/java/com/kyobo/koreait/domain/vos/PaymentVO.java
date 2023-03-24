package com.kyobo.koreait.domain.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class PaymentVO {
    private int orderNo;
    private String userEmail;
    private Date orderTime;
    private int paymentAmount;
}
