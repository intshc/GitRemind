package com.example.gitremind.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameEdit {
    private String username;
    private String githubName;

    @Builder
    public NameEdit(String username, String githubName) {
        this.username = username;
        this.githubName = githubName;
    }
}
