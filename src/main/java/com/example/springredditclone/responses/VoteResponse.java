package com.example.springredditclone.responses;

import com.example.springredditclone.utils.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    private VoteType voteType;
    private long postId;
    private String userUSerName;

}
