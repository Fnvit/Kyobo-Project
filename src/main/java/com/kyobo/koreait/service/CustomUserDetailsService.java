package com.kyobo.koreait.service;

import com.kyobo.koreait.domain.dtos.UserDTO;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(" loadUserByUsername가 실행됨.. username => " + username);
        UserVO vo = userMapper.get_user(username);
        if(vo == null){
            throw new UsernameNotFoundException("[" + username + "] 유저는 존재하지 않음");
        }

        Collection<SimpleGrantedAuthority> collection = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + vo.getRole().name()));

        return new UserDTO(
                vo.getEmail(),
                vo.getPassword(),
                vo.getName(),
                vo.getBirth(),
                vo.getPhone(),
                collection
        );
    }
}










