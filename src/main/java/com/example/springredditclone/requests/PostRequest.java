package com.example.springredditclone.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private String postName;
    private String url;
    private String description;
    private String subredditName;

}
