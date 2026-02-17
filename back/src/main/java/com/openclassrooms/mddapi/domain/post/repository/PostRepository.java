package com.openclassrooms.mddapi.domain.post.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT p
    FROM Post p
    JOIN FETCH p.subject
    JOIN FETCH p.author
    JOIN Subscription sub ON sub.subject = p.subject
    WHERE sub.user.id = :userId
    """)
    List<Post> findAllByUserSubscriptions(@Param("userId") Long userId);
}
