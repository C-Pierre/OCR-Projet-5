package com.openclassrooms.mddapi.infrastructure.post.repository.adapter;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.post.entity.Post;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.openclassrooms.mddapi.domain.post.repository.PostRepository;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class PostDataAdapterIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private PostDataAdapter adapter;
    private User author;
    private User subscriber;
    private Subject subject;
    private Post post1;

    @BeforeEach
    void setUp() {
        adapter = new PostDataAdapter(postRepository);

        long timestamp = System.currentTimeMillis();

        author = new User("author+" + timestamp + "@test.com", "author+" + timestamp);
        author.setPassword("password123");
        author = userRepository.save(author);

        subscriber = new User("subscriber+" + timestamp + "@test.com", "subscriber+" + timestamp);
        subscriber.setPassword("password123");
        subscriber = userRepository.save(subscriber);

        subject = new Subject("Math+" + timestamp, "Math description");
        Subscription subscription = new Subscription(subscriber, subject);
        subject.addSubscription(subscription);
        subject = subjectRepository.save(subject);

        post1 = new Post("Post 1", "Content 1", subject, author);
        Post post2 = new Post("Post 2", "Content 2", subject, author);

        adapter.save(post1);
        adapter.save(post2);
    }

    @Test
    void getById_shouldReturnPost() {
        Post found = adapter.getById(post1.getId());
        assertThat(found.getTitle()).isEqualTo("Post 1");
    }

    @Test
    void findAllByUserSubscriptions_shouldReturnAllPostsForSubject() {
        List<Post> posts = adapter.findAllByUserSubscriptions(subscriber.getId());
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle)
            .containsExactlyInAnyOrder("Post 1", "Post 2");
    }

    @Test
    void save_shouldPersistPost() {
        Post post3 = new Post("Post 3", "Content 3", subject, author);
        adapter.save(post3);

        Post found = postRepository.findById(post3.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("Post 3");
    }
}
