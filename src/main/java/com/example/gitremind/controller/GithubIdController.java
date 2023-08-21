package com.example.gitremind.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubIdController {

    @PostMapping("/api/github-name")
    public void insertGithubInfo(@RequestBody HashMap<String, Object> map){
        log.info(String.valueOf(map));
    }
}
