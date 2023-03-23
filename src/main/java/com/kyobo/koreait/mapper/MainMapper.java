package com.kyobo.koreait.mapper;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.dtos.HeartDTO;
import com.kyobo.koreait.domain.vos.BookVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Mapper
public interface MainMapper {
    //책 정보 가져오기
    List<BookVO> get_all_books();
    //책 정보 가져오기 (isbn을 가지는 책)
    BookVO get_book_by_isbn(String bookISBN);
    //책 정보 가져오기 (검색어 포함)
    List<BookVO> get_all_books_by_condition(
            @Param("searchKeyword") String searchKeyword,
            @Param("order") String order
    );
}








