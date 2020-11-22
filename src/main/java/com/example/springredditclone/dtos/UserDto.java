package com.example.springredditclone.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;
    private String userID;
    private String userName;
    private String email;
    private String password;
    private String encryptedPassword;
    private Instant created;
    private boolean enabled;

}
