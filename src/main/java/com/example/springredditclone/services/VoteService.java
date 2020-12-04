package com.example.springredditclone.services;

import com.example.springredditclone.dtos.VoteDto;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.utils.VoteType;

public interface VoteService {

    VoteDto createVote(VoteDto vote, long postId);

    boolean checkVoteType(PostEntity post, VoteType voteType);

}
