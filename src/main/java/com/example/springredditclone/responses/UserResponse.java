package com.example.springredditclone.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String userID;
    private String userName;
    private String email;
    private Instant created;
    private boolean enabled;

}
