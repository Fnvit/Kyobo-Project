package com.kyobo.koreait.domain.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {
    private int no;
    private String userEmail;
    private String bookISBN;
    private int bookCount;
}