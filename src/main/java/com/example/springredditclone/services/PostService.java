package com.example.springredditclone.services;

import com.example.springredditclone.dtos.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, String name, String subredditName);

    List<PostDto> getAllPosts();

    PostDto getPostById(long id);
}
