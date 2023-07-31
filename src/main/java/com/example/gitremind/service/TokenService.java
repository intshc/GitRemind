package com.example.gitremind.service;

import com.example.gitremind.domain.RefreshToken;
import com.example.gitremind.domain.User;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository tokenRepository;

    public String createAccessToken(String username) {
        return jwtUtil.createAccessToken(username, "accessToken");
    }

    public String createRefreshToken(String username) {
        return jwtUtil.createRefreshToken(username, "refreshToken");
    }

    public void saveRefreshTokenInRepo(User user, String refreshToken) {
        RefreshToken refreshTokenInRepo = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(14).toInstant(ZoneOffset.UTC))

                .build();
        tokenRepository.save(refreshTokenInRepo);
    }


}
