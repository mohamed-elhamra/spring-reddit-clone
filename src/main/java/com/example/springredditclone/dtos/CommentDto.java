package com.example.springredditclone.dtos;

import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;
    private String text;
    private Instant createdDate;
    private PostEntity post;
    private UserEntity user;

}
