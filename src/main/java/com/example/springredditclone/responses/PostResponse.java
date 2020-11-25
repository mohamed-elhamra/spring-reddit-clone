package com.example.springredditclone.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private String postName;
    private String url;
    private String description;
    private Integer voteCount;
    private Instant createdDate;
    private String userUserName;
    private String subredditName;

}
