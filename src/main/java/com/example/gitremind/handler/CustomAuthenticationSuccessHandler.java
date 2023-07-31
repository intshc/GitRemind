package com.example.gitremind.handler;

import com.example.gitremind.domain.User;
import com.example.gitremind.dto.SessionUser;
import com.example.gitremind.repository.UserRepository;
import com.example.gitremind.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

/**
 * 로그인 성공시 Access Token과 Refresh Token 발급
 */
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final HttpSession httpSession;
    private final TokenService tokenService;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
            throw new IllegalStateException("User authentication failed");
        }
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        User userFromRepo = byEmail.orElse(null);

        String accessToken = tokenService.createAccessToken(user.getName());
        String refreshToken = tokenService.createRefreshToken(user.getName());

        httpSession.setAttribute("accessToken", accessToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(1209600); // 2주
        refreshTokenCookie.setSecure(true); // 자바스크립트로 못 읽게
        refreshTokenCookie.isHttpOnly();

        tokenService.saveRefreshTokenInRepo(userFromRepo, refreshToken);

        response.setCharacterEncoding("utf-8");
        response.setHeader("accessToken", accessToken);
        response.addCookie(refreshTokenCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), user);
    }
}
