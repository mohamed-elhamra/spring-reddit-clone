package com.example.springredditclone.services;

import com.example.springredditclone.dtos.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, long postId, String userEmail);

}
