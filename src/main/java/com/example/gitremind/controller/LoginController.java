package com.example.gitremind.controller;

import com.example.gitremind.dto.SessionUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final HttpSession httpSession;

    @GetMapping("/user/api")
    public ResponseEntity<SessionUser> getData() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        if (sessionUser != null) {
            return ResponseEntity.ok(sessionUser);
        }
        return ResponseEntity.notFound().build();
    }

}
