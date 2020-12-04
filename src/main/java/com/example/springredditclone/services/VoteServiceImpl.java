package com.example.springredditclone.services;

import com.example.springredditclone.dtos.VoteDto;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.entities.VoteEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.VoteRepository;
import com.example.springredditclone.utils.Mapper;
import com.example.springredditclone.utils.VoteType;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    @Override
    public VoteDto createVote(VoteDto vote, long postId) {
        UserEntity currentUser = userService.getCurrentUser();
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("No post found with this id: " + postId));
        Optional<VoteEntity> voteByUserAndPost =
                voteRepository.findTopByPostAndUserOrderByIdDesc(post, currentUser);

        if (voteByUserAndPost.isPresent() &&
                voteByUserAndPost.get().getVoteType().equals(vote.getVoteType())) {
            throw new SpringRedditException("You have already voted " + vote.getVoteType() + " in this post.");
        }

        VoteEntity createdVote = new VoteEntity();

        if (VoteType.UPVOTE.equals(vote.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        createdVote.setPost(post);
        createdVote.setUser(currentUser);
        createdVote.setVoteType(vote.getVoteType());
        createdVote.setId(0);

        voteRepository.save(createdVote);
        postRepository.save(post);

        return Mapper.getMapper().map(createdVote, VoteDto.class);
    }

    public boolean checkVoteType(PostEntity post, VoteType voteType) {
        Optional<VoteEntity> voteForPostByUser =
                voteRepository.findTopByPostAndUserOrderByIdDesc(post,
                        userService.getCurrentUser());
        return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                .isPresent();
    }

}

