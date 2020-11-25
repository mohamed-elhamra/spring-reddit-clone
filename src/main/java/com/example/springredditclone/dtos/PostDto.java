package com.example.springredditclone.dtos;


import com.example.springredditclone.entities.SubredditEntity;
import com.example.springredditclone.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private long id;
    private String postName;
    private String url;
    private String description;
    private Integer voteCount;
    private Instant createdDate;
    private UserDto user;
    private SubredditDto subreddit;

}

