package com.openclassrooms.mddapi.subscription.entity;

import org.junit.jupiter.api.Test;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.user.repository.UserRepository;
import com.openclassrooms.mddapi.subject.repository.SubjectRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;

@DataJpaTest
@ActiveProfiles("test")
class SubscriptionTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private User createUser(String email) {
        String uniqueUserName = "user_" + System.nanoTime();
        User user = new User(email, uniqueUserName);
        user.setPassword("password123");
        return userRepository.save(user);
    }

    private Subject createSubject(String name) {
        String uniqueName = name + "_" + System.nanoTime();
        Subject subject = new Subject(uniqueName, "Subject");
        return subjectRepository.save(subject);
    }

    @Test
    void subscription_withValidUserAndSubject_shouldPersist() {
        User user = createUser("user@mail.com");
        Subject subject = createSubject("Java");

        Subscription subscription = new Subscription(user, subject);
        Subscription saved = subscriptionRepository.save(subscription);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getSubject()).isEqualTo(subject);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void subscription_ownerId_shouldReturnUserId() {
        User user = createUser("owner@mail.com");
        Subject subject = createSubject("Spring");

        Subscription subscription = new Subscription(user, subject);

        assertThat(subscription.ownerId()).isEqualTo(user.getId());
    }

    @Test
    void subscription_resourceName_shouldBeSubscription() {
        User user = createUser("resource@mail.com");
        Subject subject = createSubject("JPA");

        Subscription subscription = new Subscription(user, subject);

        assertThat(subscription.resourceName()).isEqualTo("subscription");
    }

    @Test
    void setUser_shouldUpdateUser() {
        User user1 = createUser("user1@mail.com");
        User user2 = createUser("user2@mail.com");
        Subject subject = createSubject("Docker");

        Subscription subscription = new Subscription(user1, subject);
        subscription.setUser(user2);

        assertThat(subscription.getUser()).isEqualTo(user2);
    }

    @Test
    void setSubject_shouldUpdateSubject() {
        User user = createUser("user@mail.com");
        Subject subject1 = createSubject("Kotlin");
        Subject subject2 = createSubject("Go");

        Subscription subscription = new Subscription(user, subject1);
        subscription.setSubject(subject2);

        assertThat(subscription.getSubject()).isEqualTo(subject2);
    }

    @Test
    void saveSubscription_withSameUserAndSubject_shouldThrowException() {
        User user = createUser("duplicate@mail.com");
        Subject subject = createSubject("Angular");

        Subscription sub1 = new Subscription(user, subject);
        subscriptionRepository.saveAndFlush(sub1);

        Subscription sub2 = new Subscription(user, subject);

        assertThrows(Exception.class, () ->
                subscriptionRepository.saveAndFlush(sub2)
        );
    }

    @Test
    void ownerId_shouldReturnNull_whenUserIsNull() {
        Subject subject = createSubject("Rust");
        Subscription subscription = new Subscription();
        subscription.setSubject(subject);
        subscription.setUser(null);

        assertThat(subscription.ownerId()).isNull();
    }
}
