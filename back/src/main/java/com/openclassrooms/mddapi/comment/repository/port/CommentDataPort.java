package com.openclassrooms.mddapi.comment.repository.port;

import java.util.List;
import com.openclassrooms.mddapi.comment.entity.Comment;

public interface CommentDataPort {
    List<Comment> findPostId(Long postId);
    void save(Comment comment);
}