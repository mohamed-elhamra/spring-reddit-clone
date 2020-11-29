package com.example.springredditclone.services;

import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    void verifyAccount(String token);

    UserDto getUserByEmail(String email);

    UserEntity getCurrentUser();

    List<CommentDto> getCommentsByUser(String userName);
}
