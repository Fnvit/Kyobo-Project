package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.dtos.HeartDTO;
import com.kyobo.koreait.domain.dtos.OrderDTO;
import com.kyobo.koreait.domain.enums.OrderState;
import com.kyobo.koreait.domain.vos.*;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /********************   회원가입 관련   *******************************/
    //유저를 회원가입시키는 서비스동작
    public void register_user(UserVO userVO){
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));
        userMapper.register_user(userVO);
    }

    /********************   장바구니 관련   *******************************/
    //장바구니 내용 가져오기
    public List<CartDTO> get_cart(String userEmail){
        return userMapper.get_cart(userEmail);
    }

    public boolean insert_books_in_cart(UserDetails userDetails, List<CartVO> cartVOS){
        cartVOS.parallelStream().forEach(cartDTO -> cartDTO.setUserEmail(userDetails.getUsername()));
        return userMapper.insert_books_in_cart(cartVOS);
    }

    public boolean modify_book_count_in_cart(String userEmail, CartVO cartVO){
        cartVO.setUserEmail(userEmail);
        return userMapper.modify_book_count_in_cart(cartVO);
    }
    
    public boolean modify_book_count_in_cart_by_count(String userEmail, List<CartVO> cartVOS, OrderState orderState){
        cartVOS.parallelStream().forEach(cartVO -> cartVO.setUserEmail(userEmail));
        if(orderState == OrderState.DELETE){
            cartVOS.parallelStream().forEach(cartVO -> cartVO.setBookCount(-cartVO.getBookCount()));
        }
        return userMapper.modify_book_count_in_cart_by_count(cartVOS);
    }
    

    public boolean delete_book_in_cart(String userEmail, List<CartVO> cartVOS){
        cartVOS.parallelStream().forEach(vo -> vo.setUserEmail(userEmail));
        return userMapper.delete_book_in_cart(cartVOS);
    }
    /********************   찜 관련   *******************************/
    //현재 찜 목록 전체 가져오기
    public List<BookVO> get_books_in_heart(String userEmail){
        return userMapper.get_books_in_heart(userEmail);
    }
    
    //도서 찜하기
    public boolean insert_books_in_heart(UserDetails userDetails, List<HeartDTO> heartDTOS){
        heartDTOS.parallelStream().forEach(heartDTO -> heartDTO.setUserEmail(userDetails.getUsername()));
        return userMapper.insert_books_in_heart(heartDTOS);
    }

    /********************   결제/주문 관련   *******************************/
    // 해당 유저가 결제한 결제 내역들을 가져온다 (결제 내역만 가져오고 상세 부분은 없음)
    // 주문한 유저의 정보와, 결제일시, 총 가격, 주문 번호만 가지고 있음
    public List<PaymentVO> get_payment(String userEmail){
        return userMapper.get_payment(userEmail);
    }


    //결제 내역 + 주문 내역 추가하기 (결제가 제대로 이루어졌다면 주문 내역에 추가)
    @Transactional
    public boolean insert_payment_order(String userEmail, OrderDTO orderDTO){
        //주문 유저 정보 설정하기
        PaymentVO paymentVO = orderDTO.getPaymentVO();
        paymentVO.setUserEmail(userEmail);
        //현 장바구니 리스트 가져오기
        List<CartVO> cartVOS = orderDTO.getCartVOS();
        //결제 내역에 추가하기
        boolean paymentSucceed = userMapper.insert_payment(paymentVO);
        //주문 내역에 추가하기
        boolean orderSucceed = userMapper.insert_order(cartVOS);
        //장바구니에 있는 주문 내역 삭제하기
        boolean removeSucceed = delete_book_in_cart(userEmail, cartVOS);
        //최종 결과를 반환한다
        return paymentSucceed && orderSucceed && removeSucceed;
    }

    public List<CartDTO> get_order(String orderNo){
        return userMapper.get_order(orderNo);
    }


}








