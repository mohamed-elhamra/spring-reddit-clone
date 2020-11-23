package com.example.springredditclone.services;


import com.example.springredditclone.dtos.UserDto;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.entities.VerificationTokenEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.reponses.UserResponse;
import com.example.springredditclone.repositories.UserRepository;
import com.example.springredditclone.repositories.VerificationTokenRepository;
import com.example.springredditclone.utils.IDGenerator;
import com.example.springredditclone.utils.Mapper;
import com.example.springredditclone.utils.NotificationEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
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

}