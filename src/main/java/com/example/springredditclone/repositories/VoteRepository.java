package com.example.springredditclone.repositories;

import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.entities.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    Optional<VoteEntity> findTopByPostAndUserOrderByIdDesc(PostEntity post, UserEntity user);

}
