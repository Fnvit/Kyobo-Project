package com.kyobo.koreait;

import com.kyobo.koreait.util.S3Uploader;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Log4j2
@SpringBootTest
class KyoboProjectApplicationTests {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void s3_test_upload(){
            try{
//                //String bookISBN, String dirPath, List<String> fileNames
//                String dirpath = "C:\\Users\\Administrator\\Desktop\\Web19 KSW\\spring files\\2";
//                String fileNames =
//                String uploadName = s3Uploader.upload(2, , Arrays.asList("", ""));
//            log.info(uploadName);
        }catch (Exception e){
            log.info("에러 발생!");
        }
    }
}











