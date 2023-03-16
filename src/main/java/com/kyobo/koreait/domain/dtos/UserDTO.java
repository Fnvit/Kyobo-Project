package com.kyobo.koreait.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

@Getter
@Setter
@ToString
public class UserDTO extends User {
    private String email;
    private String password;
    private String name;
    private String birth;
    private String phone;

    public UserDTO(String username, String password, String name, String birth, String phone,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
    }
}
