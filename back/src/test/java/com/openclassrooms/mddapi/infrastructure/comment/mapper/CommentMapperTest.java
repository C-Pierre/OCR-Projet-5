package com.openclassrooms.mddapi.infrastructure.comment.mapper;

import org.junit.jupiter.api.Test;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.comment.dto.CommentDto;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void toDto_shouldMapAllFields() {
        Subject subject = new Subject("Subject", "Subject");
        User author = new User("author@test.com", "author");
        Post post = new Post("Title", "Content", subject, author);
        Comment comment = new Comment("My comment", post, author);

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.content()).isEqualTo("My comment");
        assertThat(dto.authorUsername()).isEqualTo("author");
    }

    @Test
    void toDto_shouldReturnNull_whenCommentIsNull() {
        assertThat(commentMapper.toDto(null)).isNull();
    }

    @Test
    void toDto_shouldHandleNullAuthor() {
        Subject subject = new Subject("Subject", "Subject");
        Post post = new Post("Title", "Content", subject, null);

        Comment comment = new Comment("My comment", post, null);

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.authorId()).isNull();
        assertThat(dto.authorUsername()).isNull();
    }

    @Test
    void toDto_shouldHandleNullPost() {
        User author = new User("author@test.com", "author");

        Comment comment = new Comment("My comment", null, author);

        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.postId()).isNull();
        assertThat(dto.authorUsername()).isEqualTo("author");
    }
}
