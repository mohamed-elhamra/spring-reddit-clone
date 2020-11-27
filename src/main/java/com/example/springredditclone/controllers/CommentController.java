package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.requests.CommentRequest;
import com.example.springredditclone.responses.CommentResponse;
import com.example.springredditclone.services.CommentService;
import com.example.springredditclone.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest,
                                                         @org.jetbrains.annotations.NotNull Principal principal) {
        CommentDto commentDto = Mapper.getMapper().map(commentRequest, CommentDto.class);
        CommentDto createdComment = commentService.createComment(commentDto, commentRequest.getPostId() ,principal.getName());
        CommentResponse commentResponse = Mapper.getMapper().map(createdComment, CommentResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

}
