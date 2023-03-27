package com.kyobo.koreait.config;

import com.kyobo.koreait.service.CustomUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {
    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public CustomSecurityConfig(DataSource dataSource, CustomUserDetailsService customUserDetailsService) {
        this.dataSource = dataSource;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info(" ============== filterChain 실행 =============== ");
//        http.authorizeRequests().antMatchers("/user/main").authenticated();
        http.formLogin().loginPage("/user/login");
        // 로그아웃 => 내가 만든 /user/logout 으로 이동 => 세션 삭제 => 성공 시 '/'로 이동
        http.logout().logoutUrl("/user/logout").invalidateHttpSession(true).logoutSuccessUrl("/");
        http.exceptionHandling().accessDeniedPage("/error/accessDenied"); //403처리
        http.rememberMe()
                .userDetailsService(customUserDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 30);

        http.csrf();

        //카카오 로그인을 위한 로그인 페이지
        http.oauth2Login().loginPage("/user/login");
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info(" ======== create passwordEncoder ======== ");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info(" ============= webSecurityCustomizer에 의해 정적 리소스 관리 중 ============== ");
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }





}









