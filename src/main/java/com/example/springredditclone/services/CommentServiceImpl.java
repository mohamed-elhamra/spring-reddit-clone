package com.example.springredditclone.services;

import com.example.springredditclone.dtos.CommentDto;
import com.example.springredditclone.entities.CommentEntity;
import com.example.springredditclone.entities.PostEntity;
import com.example.springredditclone.entities.UserEntity;
import com.example.springredditclone.exceptions.SpringRedditException;
import com.example.springredditclone.repositories.CommentRepository;
import com.example.springredditclone.repositories.PostRepository;
import com.example.springredditclone.repositories.UserRepository;
import com.example.springredditclone.utils.Mapper;
import com.example.springredditclone.utils.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MailService mailService;
    private final UserService userService;

    @Override
    public CommentDto createComment(CommentDto commentDto, long postId, String userEmail) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new SpringRedditException("No post found with this id: " + postId));
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new SpringRedditException("No user found with this email:" + userEmail));

        CommentEntity commentEntity = Mapper.getMapper().map(commentDto, CommentEntity.class);
        commentEntity.setId(0);
        commentEntity.setCreatedDate(Instant.now());
        commentEntity.setUser(user);
        commentEntity.setPost(post);

        CommentEntity createdComment = commentRepository.save(commentEntity);

        sendCommentNotificationToPostOwner(post.getUser(), user);
        return Mapper.getMapper().map(createdComment, CommentDto.class);
    }

    private void sendCommentNotificationToPostOwner(UserEntity postOwner, UserEntity commentOwner) {
        mailService.sendMail(new NotificationEmail(commentOwner.getUserName() + " commented on your post.",
                postOwner.getEmail(),
                commentOwner.getUserName() + " posted a comment on your post. " + POST_URL));
    }
}
