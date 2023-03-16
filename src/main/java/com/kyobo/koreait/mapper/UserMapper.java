package com.kyobo.koreait.mapper;

import com.kyobo.koreait.domain.dtos.UserDTO;
import com.kyobo.koreait.domain.vos.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM `user_tbl` WHERE `email` = #{email}")
    UserVO get_user(String email);
    
    @Insert("INSERT INTO `user_tbl` VALUES " +
            "(#{email}, #{password}, #{name}, #{birth}, #{phone}, default)")
    void register_user(UserVO userVO);
}






