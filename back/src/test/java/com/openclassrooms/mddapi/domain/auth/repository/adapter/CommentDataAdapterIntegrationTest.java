package com.openclassrooms.mddapi.domain.auth.repository.adapter;

import java.util.List;

import com.openclassrooms.mddapi.infrastructure.comment.repository.adapter.CommentDataAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.comment.entity.Comment;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.comment.repository.CommentRepository;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CommentDataAdapterIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private CommentDataAdapter adapter;
    private User author;
    private Post post;

    @BeforeEach
    void setUp() {
        adapter = new CommentDataAdapter(commentRepository);

        author = new User("author@test.com", "author");
        author.setPassword("secret123");
        author = userRepository.save(author);

        Subject subject = new Subject("Subject", "Description");
        subject = subjectRepository.save(subject);

        post = new Post("Post Title", "Post Content", subject, author);
        post = postRepository.save(post);
    }

    @Test
    void save_and_findPostId_shouldPersistAndRetrieveComment() {
        Comment comment = new Comment("Integration test comment", post, author);
        adapter.save(comment);

        List<Comment> comments = adapter.findPostId(post.getId());

        assertThat(comments).isNotEmpty();
        assertThat(comments.getFirst().getContent()).isEqualTo("Integration test comment");
        assertThat(comments.getFirst().getPost().getId()).isEqualTo(post.getId());
        assertThat(comments.getFirst().getAuthor().getUserName()).isEqualTo("author");
    }
}
