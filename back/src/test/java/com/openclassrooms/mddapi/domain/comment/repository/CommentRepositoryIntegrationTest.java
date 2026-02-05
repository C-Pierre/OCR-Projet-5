package com.openclassrooms.mddapi.domain.comment.repository;

import java.util.List;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CommentRepositoryIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private User author;
    private Post post;

    @BeforeEach
    void setUp() {
        author = new User("author@test.com", "author");
        author.setPassword("secret123");
        author = userRepository.save(author);

        Subject subject = new Subject("Subject", "Description");
        subject = subjectRepository.save(subject);

        post = new Post("Post Title", "Post Content", subject, author);
        post = postRepository.save(post);

        Comment comment = new Comment("My comment", post, author);
        commentRepository.save(comment);
    }

    @Test
    void findByPostId_shouldReturnComments() {
        List<Comment> result = commentRepository.findByPostId(post.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getContent()).isEqualTo("My comment");
        assertThat(result.getFirst().getPost().getId()).isEqualTo(post.getId());
        assertThat(result.getFirst().getAuthor().getUserName()).isEqualTo("author");
    }

    @Test
    void findByPostId_shouldReturnEmpty_whenNoComments() {
        List<Comment> result = commentRepository.findByPostId(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistComment() {
        Comment newComment = new Comment("Another comment", post, author);
        Comment saved = commentRepository.save(newComment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("Another comment");
        assertThat(saved.getPost().getId()).isEqualTo(post.getId());
        assertThat(saved.getAuthor().getUserName()).isEqualTo("author");
    }
}