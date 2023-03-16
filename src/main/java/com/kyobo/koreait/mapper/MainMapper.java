package com.kyobo.koreait.mapper;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.vos.BookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    //책 정보 가져오기
    List<BookVO> get_all_books();
    //장바구니 추가
    boolean insert_cart(List<CartDTO> cartDTOS);
}








