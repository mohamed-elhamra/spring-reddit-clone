package com.example.springredditclone.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private long id;
    private String postName;
    private String url;
    private String description;
    private Integer voteCount;
    private Instant createdDate;
    private String userUserName;
    private String subredditName;
    private long commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;

}
