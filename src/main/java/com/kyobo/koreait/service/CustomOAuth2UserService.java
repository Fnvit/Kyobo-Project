package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.dtos.UserDTO;
import com.kyobo.koreait.domain.enums.UserRole;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(" ==== loadUser ==== ");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("userRequest: " + userRequest);
        log.info("clientName: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        //해당 소셜 로그인이 제공하는 key, value값들
//        paramMap.forEach((k, v) -> {
//            log.info("Attr key:" + k);
//            log.info("Attr value:" + v);
//        });
        //유저 이메일을 저장할 변수
        String userEmail = "";

        switch (clientName){
            case "kakao": //카카오 소셜 로그인을 했다면
                userEmail = get_kakao_userEmail(paramMap);
        }

        return  get_user_by_email(userEmail);
    }

    //카카오로 접속한 유저의 이메일을 반환하는 메소드
    private String get_kakao_userEmail(Map<String, Object> paramMap){
        log.info(" === KAKAO user login.. getting userEmail === ");
        LinkedHashMap kakaoAccount = (LinkedHashMap)paramMap.get("kakao_account");
        String userEmail = (String)kakaoAccount.get("email");
        log.info("가져온 userEmail ==> " + userEmail);
        return userEmail;
    }

    // 해당 kakao로그인 email을 가지는 유저가 있는지 검사하고, 해당 유저가 있다면 해당 유저를 반환하는 메소드
    private UserDTO get_user_by_email(String userEmail){
        UserVO userVO = userMapper.get_user(userEmail);
        if(userVO == null){ // DB에 해당 이메일을 사용하는 유저가 없다면
            //해당 kakao Email을 email(id)로 가지는 유저를 회원가입(등록)시킨다
            log.info("해당 유저가 존재하지 않음.. 회원가입 시도 중...");
            userVO = new UserVO(userEmail, "", "", "", "", UserRole.USER);
            userMapper.register_user(userVO);
            log.info(userEmail + " 회원 가입 완료!");
        }

        Collection<SimpleGrantedAuthority> collection = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userVO.getRole().name()));

        return new UserDTO(
                userVO.getEmail(),
                userVO.getPassword(),
                userVO.getName(),
                userVO.getBirth(),
                userVO.getPhone(),
                collection);
    }




}









