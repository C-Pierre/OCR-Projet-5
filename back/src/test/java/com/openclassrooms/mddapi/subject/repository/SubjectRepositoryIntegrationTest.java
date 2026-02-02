package com.openclassrooms.mddapi.subject.repository;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.subject.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.user.repository.UserRepository;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class SubjectRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private User user;
    private Subject subject1;
    private Subject subject2;

    @BeforeEach
    void setUp() {
        user = new User("user@test.com", "user");
        user.setPassword("secret123");
        user = userRepository.save(user); // <-- IMPORTANT

        subject1 = new Subject("Math", "Math description");
        subject2 = new Subject("Physics", "Physics description");
        subject1 = subjectRepository.save(subject1);
        subject2 = subjectRepository.save(subject2);

        Subscription subscription = new Subscription(user, subject1);

        subject1.addSubscription(subscription);
        subjectRepository.save(subject1);
    }

    @Test
    void findAllWithSubscriptionForUser_shouldMarkSubscribedCorrectly() {
        List<com.openclassrooms.mddapi.subject.dto.SubjectWithSubscriptionDto> results =
            subjectRepository.findAllWithSubscriptionForUser(user.getId());

        assertNotNull(results);
        assertEquals(2, results.size());

        var math = results.stream().filter(
            s -> Objects.equals(s.id(), subject1.getId())
        ).findFirst().orElseThrow();
        var physics = results.stream().filter(
            s -> Objects.equals(s.id(), subject2.getId())
        ).findFirst().orElseThrow();

        assertTrue(math.subscribed(), "Math should be subscribed");
        assertFalse(physics.subscribed(), "Physics should not be subscribed");
    }

    @Test
    void save_shouldPersistSubject() {
        Subject newSubject = new Subject("Chemistry", "Chemistry description");
        Subject saved = subjectRepository.save(newSubject);

        assertNotNull(saved.getId());
        assertEquals("Chemistry", saved.getName());
        assertEquals("Chemistry description", saved.getDescription());
    }

    @Test
    void findById_shouldReturnSubject_whenExists() {
        Subject found = subjectRepository.findById(subject1.getId()).orElseThrow();
        assertEquals("Math", found.getName());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        var found = subjectRepository.findById(999L);
        assertTrue(found.isEmpty());
    }
}
