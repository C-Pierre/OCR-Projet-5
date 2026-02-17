package com.openclassrooms.mddapi.domain.post.repository;

import com.openclassrooms.mddapi.domain.post.entity.Post;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private User author;
    private Subject subject;
    private Post post;

    @BeforeEach
    void setUp() {
        // Créer et sauvegarder un utilisateur
        author = new User("author@test.com", "author");
        author.setPassword("secret123");
        author = userRepository.save(author);

        // Créer et sauvegarder un sujet
        subject = new Subject("Math", "Math description");
        subject = subjectRepository.save(subject);

        // Créer et sauvegarder un post
        post = new Post("First Post", "This is the content", subject, author);
        post = postRepository.save(post);
    }

    @Test
    void save_shouldPersistPost() {
        assertThat(post.getId()).isNotNull();
        assertThat(post.getTitle()).isEqualTo("First Post");
        assertThat(post.getContent()).isEqualTo("This is the content");
        assertThat(post.getAuthor().getId()).isEqualTo(author.getId());
        assertThat(post.getSubject().getId()).isEqualTo(subject.getId());
    }

    @Test
    void findById_shouldReturnPost_whenExists() {
        Optional<Post> found = postRepository.findById(post.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("First Post");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Post> found = postRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void update_shouldModifyPostContent() {
        post.setContent("Updated content");
        postRepository.save(post);

        Post updated = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("Updated content");
    }

    @Test
    void findAll_shouldReturnPosts() {
        Post another = new Post("Second Post", "Another content", subject, author);
        postRepository.save(another);

        var posts = postRepository.findAll();
        assertThat(posts).hasSize(2);
    }
}
