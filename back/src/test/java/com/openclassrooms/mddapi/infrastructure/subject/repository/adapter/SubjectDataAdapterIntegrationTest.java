package com.openclassrooms.mddapi.infrastructure.subject.repository.adapter;

import java.util.List;
import java.util.Objects;

import com.openclassrooms.mddapi.infrastructure.subject.repository.adapter.SubjectDataAdapter;
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
import com.openclassrooms.mddapi.application.subject.dto.SubjectWithSubscriptionDto;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class SubjectDataAdapterIntegrationTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private SubjectDataAdapter adapter;
    private Subject subject1;
    private Subject subject2;
    private User user;

    @BeforeEach
    void setUp() {
        adapter = new SubjectDataAdapter(subjectRepository);

        user = new User("user@test.com", "user");
        user.setPassword("secret123");
        user = userRepository.save(user);

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
        List<SubjectWithSubscriptionDto> results = adapter.findAllWithSubscriptionForUser(user.getId());

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);

        var math = results.stream().filter(
            s -> Objects.equals(s.id(), subject1.getId())
        ).findFirst().orElseThrow();
        var physics = results.stream().filter(
            s -> Objects.equals(s.id(), subject2.getId())
        ).findFirst().orElseThrow();

        assertThat(math.subscribed()).isTrue();
        assertThat(physics.subscribed()).isFalse();
    }

    @Test
    void findAll_shouldReturnAllSubjects() {
        List<Subject> subjects = adapter.findAll();
        assertThat(subjects).hasSize(2);
    }

    @Test
    void getById_shouldReturnSubject() {
        Subject found = adapter.getById(subject1.getId());
        assertThat(found.getName()).isEqualTo("Math");
    }
}
