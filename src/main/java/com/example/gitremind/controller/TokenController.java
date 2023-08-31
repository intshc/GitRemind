package com.example.gitremind.controller;

import com.example.gitremind.domain.User;
import com.example.gitremind.dto.UserDto;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.service.TokenService;
import com.example.gitremind.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.example.gitremind.service.TokenService.getRefreshTokenInCookies;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenService tokenService;

    //accessToken 재발급
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // 쿠키에서 리프레시 토큰 가져오기
        String refreshToken = getRefreshTokenInCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token 이 쿠키에 없습니다.");
        }

        // 리프레시 토큰 검증
        boolean isTokenValid = jwtUtil.verifyToken(refreshToken, "refreshToken");

        if (isTokenValid) {
            Long id = jwtUtil.getId(refreshToken);
            String userName = jwtUtil.getUsername(refreshToken);
            String accessToken = jwtUtil.createAccessToken(userName, id, "accessToken");

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("accessToken", accessToken);

            // 클라이언트에게 응답 반환
            return ResponseEntity.ok(responseMap);
        } else {
            // 리프레시 토큰이 유효하지 않을 경우 오류 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token 유효기간이 끝났습니다.");
        }
    }

    //토큰 검증되면 사용자 정보 제공
    @GetMapping("/api/auth/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) throws Exception {
        Map<String, Object> response = new HashMap<>();
        String refreshToken = getRefreshTokenInCookies(request);

        if (refreshToken == null || !jwtUtil.verifyToken(refreshToken, "refreshToken")) {
            response.put("isValid", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Long id = jwtUtil.getId(refreshToken);
        UserDto user = userService.getUser(id);

        response.put("isValid", true);
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/access-token")
    public ResponseEntity<?> accessToken(HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        String refreshToken = getRefreshTokenInCookies(request);

        if (refreshToken == null || !jwtUtil.verifyToken(refreshToken, "refreshToken")) {
            response.put("accessToken", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        Long id = jwtUtil.getId(refreshToken);
        String userName = jwtUtil.getUsername(refreshToken);
        String accessToken = jwtUtil.createAccessToken(userName, id, "accessToken");

        response.put("accessToken", accessToken);

        return ResponseEntity.ok(response);
    }

}
