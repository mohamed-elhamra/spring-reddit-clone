package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.requests.PostRequest;
import com.example.springredditclone.responses.CommentResponse;
import com.example.springredditclone.responses.PostResponse;
import com.example.springredditclone.services.PostService;
import com.example.springredditclone.utils.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        PostResponse postResponse = postService.postDtoToPostResponse(createdPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostDto> postDtoList = postService.getAllPosts();
        List<PostResponse> postResponses = postDtoList.stream().
                map(postDto -> postService.postDtoToPostResponse(postDto))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable long id) {
        PostDto postDto = postService.getPostById(id);
        PostResponse postResponse = postService.postDtoToPostResponse(postDto);
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
