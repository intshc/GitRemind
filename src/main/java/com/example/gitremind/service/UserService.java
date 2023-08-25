package com.example.gitremind.service;

import com.example.gitremind.domain.User;
import com.example.gitremind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return user;
    }

    @Transactional
    public void setAccessToken(Long id, String token)throws Exception{
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setAccessToken(token);
        userRepository.save(user);
    }

    @Transactional
    public void setGitName(Long id, String gitName){
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setGitName(gitName);
        userRepository.save(user);
    }
}
