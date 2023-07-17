package com.example.gitremind.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("페이지가 리턴된다")
    void loginPath() throws Exception{
        //expect
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 페이지"));
    }

    @Test
    @DisplayName("유저 파라미터로 입력받기")
    void userParam() throws Exception{
        //given
        String name = "사용자";
        String email = "intshc@gmail.com";

        //expect
        mockMvc.perform(get("/login-form")
                        .param("username",name)
                        .param("email",email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(name))
                .andExpect(jsonPath("$.email").value(email));

    }
}