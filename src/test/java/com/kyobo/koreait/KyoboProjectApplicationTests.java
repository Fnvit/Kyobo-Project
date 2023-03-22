package com.kyobo.koreait;

import com.kyobo.koreait.domain.vos.CartVO;
import com.kyobo.koreait.domain.vos.UserVO;
import com.kyobo.koreait.mapper.MainMapper;
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

    @Autowired
    MainMapper mainMapper;

    @Test
    void contextLoads() {
        log.info(mainMapper.get_all_books_by_condition("명품"));


    }

}
