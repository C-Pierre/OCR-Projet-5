package com.openclassrooms.mddapi.infrastructure.post.mapper;

import org.junit.jupiter.api.Test;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.application.post.dto.PostDto;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    void toDto_shouldMapAllFields() {
        User author = new User("author@test.com", "author");
        author.setPassword("password123");
        Subject subject = new Subject("Math", "Math description");
        Post post = new Post("Post Title", "Post content", subject, author);

        PostDto dto = postMapper.toDto(post);

        assertThat(dto).isNotNull();
        assertThat(dto.title()).isEqualTo("Post Title");
        assertThat(dto.content()).isEqualTo("Post content");
        assertThat(dto.subjectName()).isEqualTo("Math");
        assertThat(dto.authorUsername()).isEqualTo("author");
        assertThat(dto.subjectId()).isNull();
        assertThat(dto.authorId()).isNull();
    }

    @Test
    void toDto_shouldReturnNull_whenPostIsNull() {
        assertThat(postMapper.toDto(null)).isNull();
    }

    @Test
    void toDto_shouldHandleNullSubject() {
        User author = new User("author@test.com", "author");
        author.setPassword("password123");
        Post post = new Post("Title", "Content", null, author);

        PostDto dto = postMapper.toDto(post);

        assertThat(dto).isNotNull();
        assertThat(dto.subjectName()).isNull();
        assertThat(dto.subjectId()).isNull();
        assertThat(dto.authorUsername()).isEqualTo("author");
    }

    @Test
    void toDto_shouldHandleNullAuthor() {
        Subject subject = new Subject("Math", "Math description");
        Post post = new Post("Title", "Content", subject, null);

        PostDto dto = postMapper.toDto(post);

        assertThat(dto).isNotNull();
        assertThat(dto.subjectName()).isEqualTo("Math");
        assertThat(dto.authorUsername()).isNull();
        assertThat(dto.authorId()).isNull();
    }

    @Test
    void toDto_shouldHandleNullAuthorAndSubject() {
        Post post = new Post("Title", "Content", null, null);

        PostDto dto = postMapper.toDto(post);

        assertThat(dto).isNotNull();
        assertThat(dto.subjectName()).isNull();
        assertThat(dto.authorUsername()).isNull();
        assertThat(dto.subjectId()).isNull();
        assertThat(dto.authorId()).isNull();
    }
}
