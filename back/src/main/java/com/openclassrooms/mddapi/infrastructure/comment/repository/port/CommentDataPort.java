package com.openclassrooms.mddapi.infrastructure.comment.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;

public interface CommentDataPort {
    List<Comment> findPostId(Long postId);
    void save(Comment comment);
}