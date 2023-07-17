package com.example.gitremind.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    private String username;
    private String email;

    @Builder
    public CreateUserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
