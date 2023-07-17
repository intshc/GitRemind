package com.example.gitremind.controller;

import com.example.gitremind.dto.CreateUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "로그인 페이지";
    }
    @GetMapping("/login-form")
    public CreateUserDto login(@RequestParam String username, @RequestParam String email){
        return CreateUserDto.builder()
                .username(username)
                .email(email).build();
    }
}
