package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.requests.RefreshTokenRequest;
import com.example.springredditclone.responses.AuthenticationResponse;
import com.example.springredditclone.responses.CommentResponse;
import com.example.springredditclone.responses.PostResponse;
import com.example.springredditclone.responses.UserResponse;
import com.example.springredditclone.requests.UserRequest;
import com.example.springredditclone.services.PostService;
import com.example.springredditclone.services.UserService;
import com.example.springredditclone.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        UserDto userDto = Mapper.getMapper().map(userRequest, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        UserResponse userResponse = Mapper.getMapper().map(createdUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        userService.verifyAccount(token);
        return ResponseEntity.ok("Account Activated Successfully");
    }

    @GetMapping("/{userName}/comments")
    public ResponseEntity<?> getCommentsByUser(@PathVariable String userName){
        List<CommentDto> comments = userService.getCommentsByUser(userName);
        List<CommentResponse> responseList = comments.stream()
                .map(commentDto -> Mapper.getMapper().map(commentDto, CommentResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{userName}/posts")
    public ResponseEntity<?> getPostsByUser(@PathVariable String userName){
        List<PostDto> posts = userService.getPostsByUser(userName);
        List<PostResponse> responseList = posts.stream()
                .map(postDto -> postService.postDtoToPostResponse(postDto))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest){
        return  ResponseEntity.ok(userService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest){
        userService.logout(refreshTokenRequest);
        return ResponseEntity.ok("Refresh Token Deleted Successfully!!");
    }
}
