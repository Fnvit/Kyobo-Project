package com.kyobo.koreait;

import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
class KyoboProjectApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        log.info(userMapper.get_cart("test"));
    }

}
