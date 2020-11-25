package com.example.springredditclone.responses;

import com.example.springredditclone.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditResponse {

    private String name;
    private String description;
    private Instant createdDate;
    private Integer postsCount;
    private UserResponse user;

}
