package com.openclassrooms.mddapi.subscription.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.subject.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class SubscriptionRepositoryIntegrationTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private com.openclassrooms.mddapi.user.repository.UserRepository userRepository;

    @Autowired
    private com.openclassrooms.mddapi.subject.repository.SubjectRepository subjectRepository;

    private User user;
    private Subject subject1;
    private Subscription subscription1;

    @BeforeEach
    void setUp() {
        user = new User("john@test.com", "john");
        user.setPassword("secret123");
        userRepository.save(user);

        subject1 = new Subject("Java", "Java programming");
        Subject subject2 = new Subject("Spring", "Spring Framework");
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);

        subscription1 = new Subscription(user, subject1);
        Subscription subscription2 = new Subscription(user, subject2);
        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
    }

    @Test
    void findByUserId_shouldReturnAllSubscriptionsForUser() {
        List<Subscription> subs = subscriptionRepository.findByUserId(user.getId());

        assertEquals(2, subs.size());
        assertTrue(subs.stream().anyMatch(s -> s.getSubject().getName().equals("Java")));
        assertTrue(subs.stream().anyMatch(s -> s.getSubject().getName().equals("Spring")));
    }

    @Test
    void findByUserIdAndSubjectId_shouldReturnSubscription_whenExists() {
        Optional<Subscription> found = subscriptionRepository.findByUserIdAndSubjectId(user.getId(), subject1.getId());

        assertTrue(found.isPresent());
        assertEquals("Java", found.get().getSubject().getName());
        assertEquals(user.getId(), found.get().getUser().getId());
    }

    @Test
    void findByUserIdAndSubjectId_shouldReturnEmpty_whenNotExists() {
        Optional<Subscription> found = subscriptionRepository.findByUserIdAndSubjectId(user.getId(), 999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void save_shouldPersistSubscription() {
        Subject newSubject = new Subject("Hibernate", "Hibernate ORM");
        subjectRepository.save(newSubject);

        Subscription newSub = new Subscription(user, newSubject);
        Subscription saved = subscriptionRepository.save(newSub);

        assertNotNull(saved.getId());
        assertEquals(user.getId(), saved.getUser().getId());
        assertEquals("Hibernate", saved.getSubject().getName());
    }

    @Test
    void delete_shouldRemoveSubscription() {
        subscriptionRepository.delete(subscription1);

        Optional<Subscription> found = subscriptionRepository.findById(subscription1.getId());
        assertTrue(found.isEmpty());
    }
}
