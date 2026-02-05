package com.openclassrooms.mddapi.infrastructure.subscription.repository.adapter;

import java.util.List;
import java.util.Optional;

import com.openclassrooms.mddapi.infrastructure.subscription.repository.adapter.SubscriptionDataAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.domain.subject.repository.SubjectRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.openclassrooms.mddapi.domain.subscription.repository.SubscriptionRepository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class SubscriptionDataAdapterIntegrationTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private SubscriptionDataAdapter adapter;
    private User user;
    private Subject subject1;
    private Subject subject2;
    private Subscription sub1;

    @BeforeEach
    void setUp() {
        adapter = new SubscriptionDataAdapter(subscriptionRepository);

        long timestamp = System.currentTimeMillis();
        user = new User("user+" + timestamp + "@test.com", "user+" + timestamp);
        user.setPassword("password123");
        user = userRepository.save(user);

        subject1 = new Subject("Java+" + timestamp, "Java programming");
        subject2 = new Subject("Spring+" + timestamp, "Spring Framework");
        subject1 = subjectRepository.save(subject1);
        subject2 = subjectRepository.save(subject2);

        sub1 = new Subscription(user, subject1);
        Subscription sub2 = new Subscription(user, subject2);
        adapter.save(sub1);
        adapter.save(sub2);
    }

    @Test
    void findByUserId_shouldReturnAllSubscriptionsForUser() {
        List<Subscription> subs = adapter.findByUserId(user.getId());

        assertThat(subs).hasSize(2);
        assertThat(subs).extracting(s -> s.getSubject().getName())
            .containsExactlyInAnyOrder(subject1.getName(), subject2.getName());
    }

    @Test
    void findByUserIdAndSubjectId_shouldReturnSubscription_whenExists() {
        Optional<Subscription> found = adapter.findByUserIdAndSubjectId(user.getId(), subject1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getSubject().getName()).isEqualTo(subject1.getName());
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserIdAndSubjectId_shouldReturnEmpty_whenNotExists() {
        Optional<Subscription> found = adapter.findByUserIdAndSubjectId(user.getId(), 999L);
        assertThat(found).isEmpty();
    }

    @Test
    void save_shouldPersistSubscription() {
        Subject newSubject = new Subject("Hibernate+" + System.currentTimeMillis(), "Hibernate ORM");
        newSubject = subjectRepository.save(newSubject);

        Subscription newSub = new Subscription(user, newSubject);
        adapter.save(newSub);

        Subscription found = subscriptionRepository.findById(newSub.getId()).orElseThrow();
        assertThat(found.getSubject().getName()).isEqualTo(newSubject.getName());
    }

    @Test
    void delete_shouldRemoveSubscription() {
        adapter.delete(sub1);
        Optional<Subscription> found = subscriptionRepository.findById(sub1.getId());
        assertThat(found).isEmpty();
    }
}
