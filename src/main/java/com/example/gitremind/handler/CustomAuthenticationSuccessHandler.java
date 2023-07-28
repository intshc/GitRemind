package com.example.gitremind.handler;

import com.example.gitremind.dto.SessionUser;
import com.example.gitremind.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SessionUser user = (SessionUser) httpSession.getAttribute("user");


        String accessToken = JwtUtil.createToken(user.getName(), "accessToken");
        String refreshToken = JwtUtil.createToken(user.getName(), "refreshToken");

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(1209600); // 2주

        response.setCharacterEncoding("utf-8");
        response.setHeader("accessToken", accessToken);
        response.addCookie(refreshTokenCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), user);
    }
}
