package com.example.springredditclone.repositories;

import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.SubredditEntity;
import com.example.springredditclone.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubreddit(SubredditEntity subredditEntity);

    List<PostEntity> findAllByUser(UserEntity userEntity);

}
