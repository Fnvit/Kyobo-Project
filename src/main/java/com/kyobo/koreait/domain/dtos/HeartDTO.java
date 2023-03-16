package com.kyobo.koreait.domain.dtos;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HeartDTO {
    private int no;
    private String userEmail;
    private String bookISBN;
}
