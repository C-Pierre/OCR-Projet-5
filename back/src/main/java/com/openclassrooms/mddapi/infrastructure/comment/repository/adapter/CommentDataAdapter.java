package com.openclassrooms.mddapi.infrastructure.comment.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import com.openclassrooms.mddapi.domain.comment.repository.CommentRepository;
import com.openclassrooms.mddapi.infrastructure.comment.repository.port.CommentDataPort;

@Service
public class CommentDataAdapter implements CommentDataPort {

    private final CommentRepository commentRepository;

    public CommentDataAdapter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findPostId(Long postId) { return commentRepository.findByPostId(postId); }

    @Override
    public void save(Comment comment) { commentRepository.save(comment); }
}

