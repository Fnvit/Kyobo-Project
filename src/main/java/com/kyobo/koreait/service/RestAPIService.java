package com.kyobo.koreait.service;

import com.kyobo.koreait.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

@Log4j2
@Service
public class RestAPIService {
    @Autowired
    private UserMapper userMapper;
    
    
    public boolean get_user(String userEmail){// 유저가 존재하면 true, 존재하지 않으면 false 반환
        return userMapper.get_user(userEmail) != null;
    }
    
    public String send_authenticate_message(String phone_number) throws Exception {
        StringBuilder smsAuthenticateNumber = new StringBuilder();
        // 0. 필요한 데이터 정의
        String requestURL = "https://sens.apigw.ntruss.com/sms/v2/services//messages";
        String method = "POST";
        String url = "/sms/v2/services//messages";
        String timestamp = Long.toString(System.currentTimeMillis());
        String accessKey = "qXFgqvRUmtsR7gjlr07K";
        String secretKey = "secretKey넣으세요";
        // 인증번호를 만들기 위한 랜덤 클래스
        for(int i = 0; i < 4; i++){
            smsAuthenticateNumber.append((int) (Math.random() * 10));
        }
        
        log.info("생성된 랜덤 인증번호 4자리 => " + smsAuthenticateNumber);
        
//        // 1. Http요청을 위해 RestTemplate 객체 선언
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 2. 응답 메세지를 받기 위해 HttpComponentsClientHttpRequestFactory 객체 생성
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setConnectTimeout(20000); //요청 timeout 20초 (ms단위)
//        factory.setReadTimeout(20000);
//
//        // 3. HttpClient 객체 생성
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnTotal(50) //최대 커넥션 수
//                .setMaxConnPerRoute(20)
//                .build();
//
//        // 4. factory에 httpclient 객체 삽입
//        factory.setHttpClient(httpClient);
//
//        // 5. restTemplate 객체에 factory 설정
//        restTemplate.setRequestFactory(factory);
//
//        // 6. 요청을 위한 HTTP Header 작성(생성)
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.set("x-ncp-apigw-timestamp", timestamp);
//        httpHeaders.set("x-ncp-iam-access-key", accessKey);
//        httpHeaders.set("x-ncp-apigw-signature-v2", makeSignature(method, url, timestamp, accessKey, secretKey));
//
//        // 7. JSON 형태의 body 데이터 생성
//        JSONObject body = new JSONObject();
//        body.put("type", "SMS");
//        body.put("countryCode", "82");
//        body.put("from", "01012345678");
//        body.put("contentType", "COMM");
//        body.put("content", "기본 메세지 내용");
//        // 모든 메세지 정보를 담은 JSONArray 객체
//        JSONArray messageArray = new JSONArray();
//        // 메세지를 보낼 한 사람의 내용을 담은 JSON 객체
//        JSONObject messageObject = new JSONObject();
//        messageObject.put("content", sms_authenticate_number);
//        messageObject.put("to", phone_number); //전달받은 휴대폰 번호에 문자 발송
//        // Array에 메세지 보낼 사람들의 정보를 넣어준다
//        messageArray.put(messageObject);
//        // messages key값은 JSON형태의 Array객체를 value로 가져야한다
//        body.put("messages", messageArray);
//
//        // 8. 위에 정의한 Header와 body정보를 가지는 HttpEntity객체 생성
//        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), httpHeaders);
//
//        // 9. HTTP 요청 => 응답받은 응답 객체를 받기위해 ResponseEntity 선언
//        // (요청 URL, 요청 Header와 body정보를 가지는 Entity객체, 응답받을 형식)
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestURL, httpEntity, String.class);
//
//        // 10. 요청 결과 출력
//        System.out.println(responseEntity);
//        System.out.println(responseEntity.getStatusCode());
//        System.out.println(responseEntity.getBody());



        return smsAuthenticateNumber.toString();
    }

    public String makeSignature(String method, String url, String timestamp, String accessKey, String secretKey) throws Exception {
        String space = " ";					// one space
        String newLine = "\n";					// new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

}









