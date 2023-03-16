package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}








