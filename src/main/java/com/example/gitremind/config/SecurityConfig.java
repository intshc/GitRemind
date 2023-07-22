package com.example.gitremind.config;

import com.example.gitremind.domain.Role;
import com.example.gitremind.service.CustomOauth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.
                        //csrf 공격 방지
                                csrf().disable().cors().and()
                        //다른 도메인의 프레임 내에서 로드되는 것을 방지
                        .headers().frameOptions().disable()
                        .and()
                        .authorizeHttpRequests()
                        .requestMatchers("/", "/css/**", "/images/**",
                                "/js/**", "/h2-console/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                        .and()
                        //로그아웃시 "/"경로로 이동
                        .logout()
                        .logoutSuccessUrl("/")
                        .and()
                        .oauth2Login()
                        //로그인 성공 이후 사용자 정보를 가져올때 설정담당
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint()
                        //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
                        //리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
                        .userService(customOauth2UserService)
                        .and().and().build();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), defaultOAuth2User);

        };
    }

}
