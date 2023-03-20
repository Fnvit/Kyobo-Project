package com.kyobo.koreait.domain.dtos;

import com.kyobo.koreait.domain.vos.BookVO;
import com.kyobo.koreait.domain.vos.CartVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private int no;
    private String bookISBN;
    private int bookCount;
    private String title;
    private int price;
}
