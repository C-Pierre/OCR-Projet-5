package com.openclassrooms.mddapi.comment.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.openclassrooms.mddapi.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
