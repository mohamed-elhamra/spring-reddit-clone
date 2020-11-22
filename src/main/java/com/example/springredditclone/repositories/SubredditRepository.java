package com.example.springredditclone.repositories;

import com.example.springredditclone.entities.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<SubredditEntity, Long> {

    Optional<SubredditEntity> findByName(String name);

}

