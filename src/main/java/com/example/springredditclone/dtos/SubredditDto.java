package com.example.springredditclone.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {

    private long id;
    private String name;
    private String description;
    private Instant createdDate;
    private List<PostDto> posts;
    private UserDto user;

}
