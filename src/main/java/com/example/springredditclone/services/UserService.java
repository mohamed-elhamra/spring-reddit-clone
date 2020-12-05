package com.example.springredditclone.services;

import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.requests.RefreshTokenRequest;
import com.example.springredditclone.responses.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    void verifyAccount(String token);

    UserDto getUserByEmail(String email);

    UserEntity getCurrentUser();

    List<CommentDto> getCommentsByUser(String userName);

    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);

    List<PostDto> getPostsByUser(String userName);

    boolean isLoggedIn();
}
