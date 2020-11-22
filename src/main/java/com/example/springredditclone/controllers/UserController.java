package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.reponses.UserResponse;
import com.example.springredditclone.requests.UserRequest;
import com.example.springredditclone.services.UserService;
import com.example.springredditclone.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
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

}
