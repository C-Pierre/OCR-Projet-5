package com.openclassrooms.mddapi.comment.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.comment.dto.CommentDto;
import com.openclassrooms.mddapi.comment.mapper.CommentMapper;
import com.openclassrooms.mddapi.comment.repository.port.CommentDataPort;

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

    public List<CommentDto> execute(Long postId) {
        return commentDataPort.findPostId(postId)
            .stream()
            .map(commentMapper::toDto)
            .toList();
    }
}
