package com.example.gitremind.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long id;
    private String githubName;
    private String username;
    private String picture;
    private String email;

    @Builder
    public UserDto(Long id, String githubName, String username, String picture, String email) {
        this.id = id;
        this.githubName = githubName;
        this.username = username;
        this.picture = picture;
        this.email = email;
    }
}
