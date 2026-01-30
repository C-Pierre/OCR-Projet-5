package com.openclassrooms.mddapi.comment.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.post.entity.Post;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import com.openclassrooms.mddapi.comment.entity.Comment;
import com.openclassrooms.mddapi.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.post.repository.port.PostDataPort;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.comment.request.CreateCommentRequest;
import com.openclassrooms.mddapi.comment.repository.port.CommentDataPort;

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

    public CommentDto execute(CreateCommentRequest request) {
        Post post = postDataPort.getById(request.postId());
        User user = userDataPort.getById(request.userId());
        Comment comment = new Comment(request.content(), post, user);

        commentDataPort.save(comment);

        return commentMapper.toDto(comment);
    }
}
