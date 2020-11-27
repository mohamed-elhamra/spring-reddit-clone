package com.example.springredditclone.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String text;
    private Instant createdDate;

}
