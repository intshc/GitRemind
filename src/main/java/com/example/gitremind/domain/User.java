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

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Builder
    public User(String username, String email, String picture, Role role) {
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }
}
