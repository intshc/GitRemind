package com.example.gitremind.config;

import com.example.gitremind.handler.CustomAuthenticationSuccessHandler;
import com.example.gitremind.jwt.JwtAuthFilter;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.repository.UserRepository;
import com.example.gitremind.service.CustomOauth2UserService;
import com.example.gitremind.service.TokenService;
import com.example.gitremind.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final HttpSession httpSession;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //csrf, frameOptions, formLogin, rememberMe, session disable, cors
        http.
                csrf().disable().cors().and()
                .headers().frameOptions().disable()
                .and().formLogin().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //요청에 대한 권한 설정
        http
                .authorizeHttpRequests()
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**", "/favicon.ico", "/login",
                        "/login/oauth2/code/**","/api/**","/auth/refresh",
                        "/api/user").permitAll()
                .anyRequest().authenticated();

        //login 관련
        http
                .oauth2Login()
                //로그인 성공 이후 쿠키, 헤더에 AccessToken 부여
                .successHandler(successHandler())
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/**")
                .and()
                .userInfoEndpoint()
                //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
                //리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
                .userService(customOauth2UserService);

        //token 관련
        http
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        //logout 관련
        http.logout()
                .logoutSuccessUrl("/")
                .clearAuthentication(true) //Authentication  객체 삭제
                .deleteCookies("refreshToken");
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler(httpSession, tokenService, userRepository,userService);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil);
    }
}
