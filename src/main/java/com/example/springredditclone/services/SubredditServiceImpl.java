package com.example.springredditclone.services;

import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.dtos.SubredditDto;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.SubredditEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.SubredditRepository;
import com.example.springredditclone.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditServiceImpl implements SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private PostRepository postRepository;

    public SubredditDto createSubreddit(SubredditDto subredditDto) {
        SubredditEntity subredditEntity = Mapper.getMapper().map(subredditDto, SubredditEntity.class);
        subredditEntity.setCreatedDate(Instant.now());
        SubredditEntity createdUser = subredditRepository.save(subredditEntity);
        return Mapper.getMapper().map(createdUser, SubredditDto.class);
    }

    @Override
    public List<SubredditDto> getAllSubreddits() {
        List<SubredditEntity> subredditEntityList = subredditRepository.findAll();
        return subredditEntityList.stream()
                .map(subredditEntity -> Mapper.getMapper().map(subredditEntity, SubredditDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubredditDto getSubredditById(long id) {
        SubredditEntity subredditEntity = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with this id: " + id));
        return Mapper.getMapper().map(subredditEntity, SubredditDto.class);
    }

    @Override
    public List<PostDto> getPostsBySubreddit(long subRedditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(subRedditId)
                .orElseThrow(() -> new UsernameNotFoundException("No subreddit found with this id: " + subRedditId));
        List<PostEntity> postEntities = postRepository.findAllBySubreddit(subredditEntity);
        return postEntities.stream()
                .map(postEntity -> Mapper.getMapper().map(postEntity, PostDto.class))
                .collect(Collectors.toList());
    }

}
