package com.example.springredditclone.services;

import com.example.springredditclone.dtos.RefreshTokenDto;
import com.example.springredditclone.entities.RefreshTokenEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.RefreshTokenRepository;
import com.example.springredditclone.utils.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenDto generateRefreshToken() {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setCreatedDate(Instant.now());

        return Mapper.getMapper()
                .map(refreshTokenRepository.save(refreshTokenEntity), RefreshTokenDto.class);
    }

    public void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
