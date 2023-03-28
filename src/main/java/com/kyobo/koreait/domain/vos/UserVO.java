package com.kyobo.koreait.domain.vos;

import com.kyobo.koreait.domain.enums.UserRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserVO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String birth;
    @NotBlank
    private String phone;
    
    private UserRole role;
}












