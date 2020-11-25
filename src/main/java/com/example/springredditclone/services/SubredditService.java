package com.example.springredditclone.services;

import com.example.springredditclone.dtos.SubredditDto;

import java.util.List;

public interface SubredditService {

    SubredditDto createSubreddit(SubredditDto subredditDto);

    List<SubredditDto> getAllSubreddits();

    SubredditDto getSubredditById(long id);
}
