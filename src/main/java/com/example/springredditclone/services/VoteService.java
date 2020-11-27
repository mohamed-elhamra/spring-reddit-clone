package com.example.springredditclone.services;

import com.example.springredditclone.dtos.VoteDto;

public interface VoteService {

    VoteDto createVote(VoteDto vote, long postId);

}
