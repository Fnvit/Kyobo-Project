package com.kyobo.koreait.domain.dtos;

import com.kyobo.koreait.domain.vos.BookVO;
import com.kyobo.koreait.domain.vos.CartVO;
import com.kyobo.koreait.domain.vos.OrderVO;
import com.kyobo.koreait.domain.vos.PaymentVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class OrderDTO {
    private PaymentVO paymentVO;
    private List<CartVO> cartVOS;
}






