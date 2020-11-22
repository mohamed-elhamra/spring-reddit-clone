package com.example.springredditclone.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = "User name should not be null")
    @Size(min = 3, max = 50, message = "Size should be between 3 and 50 character")
    private String userName;

    @NotNull(message = "Email should not be null")
    @Email(message = "Email format is incorrect")
    private String email;

    @NotNull(message = "Password name should not be null")
    @Size(min = 4, max = 12, message = "Size should be between 8 and 12 character")
    private String password;

}
