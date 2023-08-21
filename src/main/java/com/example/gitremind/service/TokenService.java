package com.example.gitremind.service;

import com.example.gitremind.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    public String createAccessToken(String username,Long id) {
        return jwtUtil.createAccessToken(username,id, "accessToken");
    }

    public String createRefreshToken(String username,Long id) {
        return jwtUtil.createRefreshToken(username, id,"refreshToken");
    }

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(1209600); // 2주
        refreshTokenCookie.setSecure(true); // 자바스크립트로 못 읽게

        return refreshTokenCookie;
    }
    public static String getRefreshTokenInCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}
