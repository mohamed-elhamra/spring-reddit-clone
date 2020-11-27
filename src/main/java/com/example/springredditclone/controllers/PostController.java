package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.requests.PostRequest;
import com.example.springredditclone.responses.CommentResponse;
import com.example.springredditclone.responses.PostResponse;
import com.example.springredditclone.services.PostService;
import com.example.springredditclone.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest, Principal principal) {
        PostDto postDto = Mapper.getMapper().map(postRequest, PostDto.class);
        PostDto createdPost = postService.createPost(postDto, principal.getName(), postRequest.getSubredditName());
        PostResponse postResponse = Mapper.getMapper().map(createdPost, PostResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostDto> postDtoList = postService.getAllPosts();
        List<PostResponse> postResponses = postDtoList.stream().
                map(postDto -> Mapper.getMapper().map(postDto, PostResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable long id) {
        PostDto postDto = postService.getPostById(id);
        PostResponse postResponse = Mapper.getMapper().map(postDto, PostResponse.class);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?>getAllCommentsByPost(@PathVariable long postId){
        List<CommentDto> comments = postService.getCommentsByPost(postId);
        List<CommentResponse> responseList = comments.stream()
                .map(commentDto -> Mapper.getMapper().map(commentDto, CommentResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

}
