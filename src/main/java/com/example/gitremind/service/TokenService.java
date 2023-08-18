package com.example.gitremind.service;

import com.example.gitremind.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    public String createAccessToken(String username) {
        return jwtUtil.createAccessToken(username, "accessToken");
    }

    public String createRefreshToken(String username) {
        return jwtUtil.createRefreshToken(username, "refreshToken");
    }

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(1209600); // 2주
        refreshTokenCookie.setSecure(true); // 자바스크립트로 못 읽게

        return refreshTokenCookie;
    }

}
