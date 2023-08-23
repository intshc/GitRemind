package com.example.gitremind.dto;

import com.example.gitremind.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    //인증된 사용자 정보
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public SessionUser(User user) {
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

}
