package com.example.gitremind.controller;

import com.example.gitremind.dto.UserDto;
import com.example.gitremind.jwt.JwtUtil;
import com.example.gitremind.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.gitremind.service.TokenService.getRefreshTokenInCookies;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubIdController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/github-name")
    public void insertGithubInfo(@RequestBody UserDto userDto, HttpServletRequest request){
        // 쿠키에서 리프레시 토큰 가져오기
        String refreshToken = getRefreshTokenInCookies(request);
        Long id = jwtUtil.getId(refreshToken);
        userService.setGitName(id, userDto.getGithubName());
    }
}
