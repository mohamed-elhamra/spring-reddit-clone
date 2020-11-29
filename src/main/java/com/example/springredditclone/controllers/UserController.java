package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.responses.CommentResponse;
import com.example.springredditclone.responses.UserResponse;
import com.example.springredditclone.requests.UserRequest;
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

}
