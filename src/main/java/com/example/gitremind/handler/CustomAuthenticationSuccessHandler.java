package com.example.gitremind.handler;

import com.example.gitremind.domain.User;
import com.example.gitremind.dto.SessionUser;
import com.example.gitremind.repository.UserRepository;
import com.example.gitremind.service.TokenService;
import com.example.gitremind.service.UserService;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 성공시 Access Token과 Refresh Token 발급
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpSession httpSession;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
            throw new IllegalStateException("User authentication failed");
        }
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        User userFromRepo = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long id = userFromRepo.getId();
        String refreshToken = tokenService.createRefreshToken(user.getName(),id);

        Cookie refreshTokenCookie = tokenService.createRefreshTokenCookie(refreshToken);

        //provider 구하기 예)naver, google, github
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        String provider = oauth2Token.getAuthorizedClientRegistrationId();
        String redirect_uri = "http://localhost:3000/login/oauth2/code/" + provider;

        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect(redirect_uri);

        clearAuthenticationAttributes(request);// 세션 제거
    }
}
