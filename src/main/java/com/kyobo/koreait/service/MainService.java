package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.vos.BookVO;
import com.kyobo.koreait.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {
    @Autowired
    private MainMapper mapper;
    //책 정보 가져오기
    public List<BookVO> get_all_books(){
        return mapper.get_all_books();
    }

    //장바구니추가
    public boolean insert_cart(UserDetails userDetails, List<CartDTO> cartDTOS){
        cartDTOS.parallelStream().forEach(cartDTO -> cartDTO.setUserEmail(userDetails.getUsername()));

        return mapper.insert_cart(cartDTOS);
    }
}











