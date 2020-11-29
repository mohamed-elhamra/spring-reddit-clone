package com.example.springredditclone.services;

import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.entities.CommentEntity;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.SubredditEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.CommentRepository;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.SubredditRepository;
import com.example.springredditclone.repositories.UserRepository;
import com.example.springredditclone.responses.PostResponse;
import com.example.springredditclone.responses.UserResponse;
import com.example.springredditclone.utils.Mapper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public PostDto createPost(PostDto postDto, String email, String subredditName) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this email: " + email));
        SubredditEntity subredditEntity = subredditRepository.findByName(subredditName)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with this name: " + subredditName));

        PostEntity postEntity = Mapper.getMapper().map(postDto, PostEntity.class);
        postEntity.setCreatedDate(Instant.now());
        postEntity.setUser(userEntity);
        postEntity.setSubreddit(subredditEntity);
        postEntity.setVoteCount(0);

        PostEntity createdPost = postRepository.save(postEntity);
        return Mapper.getMapper().map(createdPost, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        return postEntities.stream()
                .map(postEntity -> Mapper.getMapper().map(postEntity, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No post found with this id: " + id));
        return Mapper.getMapper().map(postEntity, PostDto.class);
    }

    @Override
    public List<CommentDto> getCommentsByPost(long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("No post found with this id: " + postId));
        List<CommentEntity> commentEntities = commentRepository.findAllByPost(post);

        return commentEntities.stream()
                .map(commentEntity -> Mapper.getMapper().map(commentEntity, CommentDto.class))
                .collect(Collectors.toList());
    }

    public PostResponse postDtoToPostResponse(PostDto postDto){
        PostEntity postEntity = Mapper.getMapper().map(postDto, PostEntity.class);
        long numberOfCommentsInPost = commentRepository.findAllByPost(postEntity).size();

        String postDuration = getDuration(postDto);

        return PostResponse.builder().postName(postDto.getPostName())
                .url(postDto.getUrl()).description(postDto.getDescription())
                .voteCount(postDto.getVoteCount()).createdDate(postDto.getCreatedDate())
                .userUserName(postDto.getUser().getUserName())
                .subredditName(postDto.getSubreddit().getName())
                .commentCount(numberOfCommentsInPost).duration(postDuration)
                .build();
    }

    private String getDuration(PostDto postDto){
        return TimeAgo.using(postDto.getCreatedDate().toEpochMilli());
    }

}
