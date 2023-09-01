package com.example.gitremind.controller;

import com.example.gitremind.dto.NameEdit;
import com.example.gitremind.dto.UserDto;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.gitremind.service.TokenService.getRefreshTokenInCookies;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/user")
    public ResponseEntity<UserDto> getData(HttpServletRequest request) {
        String refreshToken = getRefreshTokenInCookies(request);
        Long id = jwtUtil.getId(refreshToken);
        UserDto user = userService.getUser(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/api/user")
    public void patchData(HttpServletRequest request,@RequestBody NameEdit edit) {
        String refreshToken = getRefreshTokenInCookies(request);
        Long id = jwtUtil.getId(refreshToken);

        userService.editName(id, edit);

    }
}
