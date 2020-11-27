package com.example.springredditclone.requests;

import com.example.springredditclone.utils.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    private VoteType voteType;
    private long postId;

}
