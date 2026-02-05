package com.openclassrooms.mddapi.domain.post.entity;

import java.util.Set;

import com.openclassrooms.mddapi.domain.post.entity.Post;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@ActiveProfiles("test")
class PostTest {

    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void post_withValidFields_shouldPassValidation() {
        User author = new User("author@mail.com", "author");
        author.setPassword("password123");
        author = userRepository.save(author);

        Subject subject = new Subject("Math", "Math description");
        subject = subjectRepository.save(subject);

        Post post = new Post("Post Title", "Post content", subject, author);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertThat(violations).isEmpty();
    }

    @Test
    void post_withBlankTitleOrContent_shouldFailValidation() {
        User author = new User("author2@mail.com", "author2");
        author.setPassword("password123");
        author = userRepository.save(author);
        Subject subject = subjectRepository.save(new Subject("Physics", "Physics description"));

        Post post = new Post("", "", subject, author);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertThat(violations).hasSize(2); // title et content non valides
    }

    @Test
    void savePost_shouldPersistData() {
        User author = new User("author3@mail.com", "author3");
        author.setPassword("password123");
        author = userRepository.save(author);
        Subject subject = subjectRepository.save(new Subject("Chemistry", "Chemistry description"));

        Post post = new Post("Chem Post", "Some content", subject, author);
        Post saved = postRepository.save(post);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Chem Post");
        assertThat(saved.getContent()).isEqualTo("Some content");
        assertThat(saved.getAuthor().getUserName()).isEqualTo("author3");
        assertThat(saved.getSubject().getName()).isEqualTo("Chemistry");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void setContent_shouldUpdateValue() {
        User author = new User("author4@mail.com", "author4");
        author.setPassword("password123");
        author = userRepository.save(author);
        Subject subject = subjectRepository.save(new Subject("Biology", "Biology description"));

        Post post = new Post("Bio Post", "Initial content", subject, author);
        Post saved = postRepository.save(post);

        saved.setContent("Updated content");
        postRepository.save(saved);

        Post updated = postRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("Updated content");
    }

    @Test
    void setSubject_shouldUpdateValue() {
        User author = new User("author5@mail.com", "author5");
        author.setPassword("password123");
        author = userRepository.save(author);
        Subject subject1 = subjectRepository.save(new Subject("History", "History description"));
        Subject subject2 = subjectRepository.save(new Subject("Geography", "Geo description"));

        Post post = new Post("Hist Post", "Content", subject1, author);
        Post saved = postRepository.save(post);

        saved.setSubject(subject2);
        postRepository.save(saved);

        Post updated = postRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getSubject().getName()).isEqualTo("Geography");
    }
}
