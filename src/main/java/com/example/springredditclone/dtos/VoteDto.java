package com.example.springredditclone.dtos;

import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.utils.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private long id;
    private VoteType voteType;
    private PostEntity post;
    private UserEntity user;

}
