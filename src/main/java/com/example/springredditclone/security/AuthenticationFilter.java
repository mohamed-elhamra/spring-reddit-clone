package com.example.springredditclone.security;

import com.example.springredditclone.SpringApplicationContext;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.requests.UserLoginRequest;
import com.example.springredditclone.responses.AuthenticationResponse;
import com.example.springredditclone.services.RefreshTokenService;
import com.example.springredditclone.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequest userLoginRequest = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User springUser = (User) authResult.getPrincipal();
        String email = springUser.getUsername();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUserByEmail(email);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("id", userDto.getUserID())
                .claim("username", userDto.getUserName())
                .claim("roles", springUser.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();

        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("user_id", userDto.getUserID());

        String authenticationResponseJsonString = getAuthenticationResponseJsonString(token, userDto.getUserName());
        response.getWriter().write(authenticationResponseJsonString);
    }

    private String getAuthenticationResponseJsonString(String jwtToken, String userName) {
        RefreshTokenService refreshTokenService =
                (RefreshTokenService) SpringApplicationContext.getBean("refreshTokenService");
        String token = refreshTokenService.generateRefreshToken().getToken();

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticationToken(jwtToken).refreshToken(token)
                .expiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .email(userName).build();

        Gson gson = new Gson();
        return gson.toJson(authenticationResponse);
    }
}
