package com.kyobo.koreait.domain.dtos;

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
    private String userEmail;
    private String bookISBN;
    private int bookCount;
}
