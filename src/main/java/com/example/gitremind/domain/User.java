package com.example.gitremind.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "`GitUser`")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String username;

    //깃허브 로그인 시에 이메일이 없는 경우도 있음
    @Column
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String gitName;

    @Builder
    public User(String username, String email, String picture, Role role, String gitName) {
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.gitName = gitName;
    }

    public User update(String username, String picture) {
        this.username = username;
        this.picture = picture;
        return this;
    }

    public void editName(String username, String gitName){
        this.username = username;
        this.gitName = gitName;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }


    public void setGitName(String gitName){
        this.gitName=gitName;
    }
}
