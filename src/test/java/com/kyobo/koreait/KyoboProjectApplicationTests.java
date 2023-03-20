package com.kyobo.koreait;

import com.kyobo.koreait.domain.vos.CartVO;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@SpringBootTest
class KyoboProjectApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        CartVO vo = new CartVO();
        vo.setUserEmail("test");
        vo.setBookISBN("10");
        vo.setBookCount(3);

        CartVO vo3 = new CartVO();
        vo3.setUserEmail("test");
        vo3.setBookISBN("12");

        List<CartVO> cartVOList = Arrays.asList(vo, vo3);
        userMapper.delete_book_in_cart(cartVOList);


    }

}
