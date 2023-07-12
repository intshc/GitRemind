package com.example.gitremind.repository;

import com.example.gitremind.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, Long> {

}
