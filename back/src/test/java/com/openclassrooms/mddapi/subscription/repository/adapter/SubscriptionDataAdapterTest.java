package com.openclassrooms.mddapi.subscription.repository.adapter;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.subscription.repository.SubscriptionRepository;

class SubscriptionDataAdapterTest {

    private SubscriptionRepository subscriptionRepository;
    private SubscriptionDataAdapter adapter;

    private User user;
    private Subject subject;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        adapter = new SubscriptionDataAdapter(subscriptionRepository);

        // Création d'un user et d'un subject pour les subscriptions
        user = new User("user@test.com", "user");
        subject = new Subject("Java", "Java programming");
    }

    @Test
    void findByUserIdAndSubjectId_shouldDelegateToRepository() {
        Subscription sub = new Subscription(user, subject);
        Long userId = 1L;
        Long subjectId = 10L;

        when(subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.of(sub));

        Optional<Subscription> result = adapter.findByUserIdAndSubjectId(userId, subjectId);

        assertThat(result).contains(sub);
        verify(subscriptionRepository, times(1)).findByUserIdAndSubjectId(userId, subjectId);
    }

    @Test
    void findByUserId_shouldDelegateToRepository() {
        Subscription sub1 = new Subscription(user, subject);
        Subscription sub2 = new Subscription(user, subject);
        List<Subscription> subs = List.of(sub1, sub2);
        Long userId = 1L;

        when(subscriptionRepository.findByUserId(userId)).thenReturn(subs);

        List<Subscription> result = adapter.findByUserId(userId);

        assertThat(result).isSameAs(subs);
        verify(subscriptionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void save_shouldDelegateToRepository() {
        Subscription sub = new Subscription(user, subject);
        adapter.save(sub);
        verify(subscriptionRepository, times(1)).save(sub);
    }

    @Test
    void delete_shouldDelegateToRepository() {
        Subscription sub = new Subscription(user, subject);
        adapter.delete(sub);
        verify(subscriptionRepository, times(1)).delete(sub);
    }
}
