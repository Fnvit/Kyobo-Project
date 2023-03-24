package com.kyobo.koreait.controller;

import com.kyobo.koreait.domain.dtos.CartDTO;
import com.kyobo.koreait.domain.dtos.HeartDTO;
import com.kyobo.koreait.domain.dtos.OrderDTO;
import com.kyobo.koreait.domain.vos.CartVO;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /******************** 로그인 관련 **********************/
    
    @GetMapping("/login")
    public void login_user_get(){
        
    }
    
    @PostMapping("/login")
    public String login_user_post(){
        return "redirect:/";
    }
    
    
    /******************** 로그아웃 관련 **********************/

    @GetMapping("/logout")
    public void logout(){
        log.info(" ====== 유저 로그아웃(logout) ====== ");
    }


    /******************** 회원가입 관련 **********************/
    @GetMapping("/register")
    public void register_user(){
    }

    @PostMapping("/register")
    public String register_user(@Validated UserVO userVO,
                                BindingResult bindingResult,
                                HttpSession session){
        log.info(" ====== register_user ====== ");
        log.info(" 받아온 userVO => " + userVO);
        if(bindingResult.hasErrors()){
            log.info(" bindingResult에서 에러가 발생하였음 ");
            return "/error/main";
        }
        //인증받은 인증 번호와 중복체크한 유저이메일 (아이디)를 가져온다
        Boolean phoneAuthenticated = (Boolean) session.getAttribute("phoneAuthenticated"); // 인증이 되었는지 체크
        String phoneAuthenticatedNumber = (String)session.getAttribute("phoneAuthenticatedNumber"); //휴대폰 번호 일치 체크
        String userEmail = (String)session.getAttribute("emailAuthenticated"); //이메일 일치 체크
        log.info("phoneAuthenticated: " + phoneAuthenticated);
        log.info("phoneAuthenticatedNumber: " + phoneAuthenticatedNumber);
        log.info("userEmail: " + userEmail);
        if(phoneAuthenticated == null // 휴대폰 인증 거치지 않고 왔거나
                || userEmail == null // 이메일 중복체크를 거치치 않고 왔거나
                || !phoneAuthenticated // 인증이 false거나 (실패했거나)
                || !userVO.getEmail().equals(userEmail) //인증받은 이메일과 가입할 이메일이 다르거나
                || !userVO.getPhone().equals(phoneAuthenticatedNumber)) { //인증받은 휴대폰과 가입할 휴대폰이 다르거나
            log.info("에러!!");
            return "/error/main";
        }

        log.info(" 유저 회원가입을 시도함...");
        userService.register_user(userVO);
        log.info(" ====> 유저 회원가입이 완료되었음");
        return "redirect:/";
    }


    /******************** 마이페이지 관련 **********************/
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/main")
    public void mypage_main(){
        log.info(" ====== mypage_main - 유저의 마이페이지 메인화면 ======= ");
        
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/heart")
    public void mypage_heart(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ){
        log.info(" ====== mypage_heart - 유저의 마이페이지 - 찜 목록 화면 ======= ");
        model.addAttribute("bookVOS", userService.get_books_in_heart(userDetails.getUsername()));
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/order")
    public void mypage_order(){
        log.info(" ====== mypage_order - 유저의 마이페이지 - 주문/배송 메인화면 ======= ");

    }
    
    
    
    /******************** 장바구니 관련 **********************/
    

    @ResponseBody
    @GetMapping("/cart")
    public List<CartDTO> get_cart(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return userService.get_cart(userDetails.getUsername());
    }
    
    @ResponseBody
    @PostMapping("/cart")
    public boolean insert_cart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<CartVO> cartVOS
    ){
        log.info(" ==== insert_cart ==== ");
        return userService.insert_books_in_cart(userDetails, cartVOS);
    }

    @ResponseBody
    @PutMapping("/cart")
    public boolean modify_cart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartVO cartVO
    ){
        log.info(" ==== modify_cart ==== ");
        log.info(cartVO);
        return userService.modify_book_count_in_cart(userDetails.getUsername(), cartVO);
    }

    @ResponseBody
    @DeleteMapping("/cart")
    public boolean delete_cart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<CartVO> cartVOS
    ){
        log.info(" delete_cart - 장바구니 삭제 ");
        return userService.delete_book_in_cart(userDetails.getUsername(), cartVOS);
    }

    /******************** 찜 관련 **********************/    
    @ResponseBody
    @PostMapping("/heart")
    public boolean insert_heart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<HeartDTO> heartDTOS
    ){
        log.info(" ===== insert_heart ===== ");
        return userService.insert_books_in_heart(userDetails, heartDTOS);
    }

    /******************** 주문 관련 **********************/
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/order")
    public String insert_order(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderDTO orderDTO){
        log.info(" ======== insert_order - 상품 주문하기 ======== ");
        //현재 로그인된 유저 정보와 javascript에서 받아온 DTO객체 정보를 넘겨줌
        boolean orderResult = userService.insert_payment_order(userDetails.getUsername(), orderDTO);
        if(!orderResult){
            return "/error/main";
        }
        return "/main/order";
    }




}










