package com.example.springredditclone.requests;

import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotBlank
    private String text;
    private long postId;

}
