package com.example.gitremind.controller;

import com.example.gitremind.domain.User;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.gitremind.service.TokenService.getRefreshTokenInCookies;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/api")
    public ResponseEntity<User> getData(HttpServletRequest request) throws Exception {
        String refreshToken = getRefreshTokenInCookies(request);
        Long id = jwtUtil.getId(refreshToken);
        User user = userService.getUser(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

}
