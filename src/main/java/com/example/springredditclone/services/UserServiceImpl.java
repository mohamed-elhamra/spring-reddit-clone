package com.example.springredditclone.services;


import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.entities.CommentEntity;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.entities.VerificationTokenEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.CommentRepository;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.UserRepository;
import com.example.springredditclone.repositories.VerificationTokenRepository;
import com.example.springredditclone.requests.RefreshTokenRequest;
import com.example.springredditclone.responses.AuthenticationResponse;
import com.example.springredditclone.security.SecurityConstants;
import com.example.springredditclone.utils.IDGenerator;
import com.example.springredditclone.utils.Mapper;
import com.example.springredditclone.utils.NotificationEmail;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PostRepository postRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.isEnabled(),
                true, true, true, authorities);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new RuntimeException("User already exists!");

        UserEntity user = Mapper.getMapper().map(userDto, UserEntity.class);

        user.setUserID(idGenerator.generateStringId(32));
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setCreated(Instant.now());

        UserEntity createdUser = userRepository.save(user);

        String token = generateVerificationToken(createdUser);
        mailService.sendMail(new NotificationEmail("Please activate your account", createdUser.getEmail(),
                "Thank you for signing up to Spring Reddit, " +
                        "please click on the below url to activate your account : " +
                        "http://localhost:8080/api/users/accountVerification/" + token));

        return Mapper.getMapper().map(createdUser, UserDto.class);
    }

    private String generateVerificationToken(UserEntity createdUser) {
        String token = UUID.randomUUID().toString();
        VerificationTokenEntity verificationToken = new VerificationTokenEntity();
        verificationToken.setToken(token);
        verificationToken.setUser(createdUser);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void verifyAccount(String token) {
        Optional<VerificationTokenEntity> verificationTokenEntity = verificationTokenRepository.findByToken(token);
        verificationTokenEntity.orElseThrow(() -> new SpringRedditException("Invalid token"));
        String email = verificationTokenEntity.get().getUser().getEmail();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new SpringRedditException("User not found with email - " + email));
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));
        return Mapper.getMapper().map(userEntity, UserDto.class);
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this email: " + authentication.getName()));
    }

    @Override
    public List<CommentDto> getCommentsByUser(String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this user name: " + userName));
        List<CommentEntity> commentEntities = commentRepository.findAllByUser(user);
        return commentEntities.stream()
                .map(commentEntity -> Mapper.getMapper().map(commentEntity, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getToken());
        UserDetails springUser = loadUserByUsername(refreshTokenRequest.getEmail());
        UserDto userDto = getUserByEmail(refreshTokenRequest.getEmail());

        String jwt = Jwts.builder()
                .setSubject(refreshTokenRequest.getEmail())
                .claim("id", userDto.getUserID())
                .claim("username", userDto.getUserName())
                .claim("roles", springUser.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();

        return AuthenticationResponse.builder()
                .authenticationToken(jwt).refreshToken(refreshTokenRequest.getToken())
                .expiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .email(refreshTokenRequest.getEmail()).build();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getToken());
    }

    @Override
    public List<PostDto> getPostsByUser(String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this user name: " + userName));
        List<PostEntity> postEntities = postRepository.findAllByUser(user);
        return postEntities.stream()
                .map(postEntity -> Mapper.getMapper().map(postEntity, PostDto.class))
                .collect(Collectors.toList());
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
