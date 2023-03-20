package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.dtos.HeartDTO;
import com.kyobo.koreait.domain.vos.CartVO;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //유저를 회원가입시키는 서비스동작
    public void register_user(UserVO userVO){
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));
        userMapper.register_user(userVO);
    }
    
    //장바구니 내용 가져오기
    public List<CartDTO> get_cart(String userEmail){
        return userMapper.get_cart(userEmail);
    }

    //장바구니추가
    public boolean insert_books_in_cart(UserDetails userDetails, List<CartVO> cartVOS){
        cartVOS.parallelStream().forEach(cartDTO -> cartDTO.setUserEmail(userDetails.getUsername()));
        return userMapper.insert_books_in_cart(cartVOS);
    }

    public boolean modify_book_count_in_cart(String userEmail, CartVO cartVO){
        cartVO.setUserEmail(userEmail);
        return userMapper.modify_book_count_in_cart(cartVO);
    }

    public boolean delete_book_in_cart(UserDetails userDetails, List<CartVO> cartVOS){
        cartVOS.parallelStream().forEach(vo -> vo.setUserEmail(userDetails.getUsername()));
        return userMapper.delete_book_in_cart(cartVOS);
    }

    //찜하기
    public boolean insert_books_in_heart(UserDetails userDetails, List<HeartDTO> heartDTOS){
        heartDTOS.parallelStream().forEach(heartDTO -> heartDTO.setUserEmail(userDetails.getUsername()));
        return userMapper.insert_books_in_heart(heartDTOS);
    }
}








