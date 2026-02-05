package com.openclassrooms.mddapi.application.comment.service;

import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import com.openclassrooms.mddapi.application.comment.dto.CommentDto;
import com.openclassrooms.mddapi.api.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.infrastructure.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.infrastructure.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.comment.repository.port.CommentDataPort;

@Service
public class CreateCommentService {

    private final CommentMapper commentMapper;
    private final PostDataPort postDataPort;
    private final CommentDataPort commentDataPort;
    private final UserDataPort userDataPort;

    public CreateCommentService(
        CommentMapper commentMapper,
        PostDataPort postDataPort,
        CommentDataPort commentDataPort,
        UserDataPort userDataPort
    ) {
        this.commentMapper = commentMapper;
        this.postDataPort = postDataPort;
        this.commentDataPort = commentDataPort;
        this.userDataPort = userDataPort;
    }

    @Caching(evict = {
        @CacheEvict(value = "commentsCache", allEntries = true),
        @CacheEvict(value = "commentsPostCache", key = "#id")
    })
    public CommentDto execute(CreateCommentRequest request) {
        Post post = postDataPort.getById(request.postId());
        User user = userDataPort.getById(request.userId());
        Comment comment = new Comment(request.content(), post, user);

        commentDataPort.save(comment);

        return commentMapper.toDto(comment);
    }
}
