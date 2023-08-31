package com.example.gitremind.service;

import com.example.gitremind.domain.User;
import com.example.gitremind.dto.UserDto;
import com.example.gitremind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return UserDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .githubName(user.getGitName())
                .picture(user.getPicture())
                .build();
    }

    @Transactional
    public void setGitName(Long id, String gitName){
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setGitName(gitName);
        userRepository.save(user);
    }

}
