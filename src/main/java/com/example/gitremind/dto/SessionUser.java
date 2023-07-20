package com.example.gitremind.dto;

import com.example.gitremind.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    //인증된 사용자 정보
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
