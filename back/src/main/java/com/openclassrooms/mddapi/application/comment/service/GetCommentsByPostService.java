package com.openclassrooms.mddapi.application.comment.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.application.comment.dto.CommentDto;
import com.openclassrooms.mddapi.infrastructure.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.infrastructure.comment.repository.port.CommentDataPort;

@Service
public class GetCommentsByPostService {

    private final CommentMapper commentMapper;
    private final CommentDataPort commentDataPort;

    public GetCommentsByPostService(
        CommentMapper commentMapper,
        CommentDataPort commentDataPort
    ) {
        this.commentMapper = commentMapper;
        this.commentDataPort = commentDataPort;
    }

    @Cacheable(value = "commentsPostCache", key = "#id")
    public List<CommentDto> execute(Long postId) {
        return commentDataPort.findPostId(postId)
            .stream()
            .map(commentMapper::toDto)
            .toList();
    }
}
