//package com.example.gitremind.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return
//                http.
//                        //csrf 공격 방지
//                        csrf().disable()
//                        //다른 도메인의 프레임 내에서 로드되는 것을 방지
//                        .headers().frameOptions().disable()
//                        .and()
//                        //로그인기능 x
//                        .authorizeHttpRequests()
//                        .anyRequest().permitAll()
//                        .and()
//                        //로그아웃시 "/"경로로 이동
//                        .logout()
//                        .logoutSuccessUrl("/")
//                        .and()
//                        .build();
//    }
//}
