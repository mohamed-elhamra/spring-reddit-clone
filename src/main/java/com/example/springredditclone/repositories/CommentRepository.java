package com.example.springredditclone.repositories;

import com.example.springredditclone.entities.CommentEntity;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPost(PostEntity post);

    List<CommentEntity> findAllByUser(UserEntity user);

}
