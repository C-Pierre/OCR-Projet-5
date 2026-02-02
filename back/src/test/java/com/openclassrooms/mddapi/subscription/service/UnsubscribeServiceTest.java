package com.openclassrooms.mddapi.subscription.service;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.subscription.request.UnsubscribeRequest;
import com.openclassrooms.mddapi.common.exception.type.BadRequestException;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

class UnsubscribeServiceTest {

    private SubscriptionDataPort subscriptionDataPort;
    private UnsubscribeService service;

    @BeforeEach
    void setUp() {
        subscriptionDataPort = mock(SubscriptionDataPort.class);
        service = new UnsubscribeService(subscriptionDataPort);
    }

    @Test
    void execute_shouldDeleteSubscription_whenExists() {
        User user = new User("john@test.com", "john");
        Long userId = user.getId();

        Subject subject = new Subject("Java", "Java programming");
        Long subjectId = subject.getId();

        Subscription subscription = new Subscription(user, subject);

        UnsubscribeRequest request = new UnsubscribeRequest(subjectId, userId);

        when(subscriptionDataPort.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.of(subscription));

        service.execute(request);

        verify(subscriptionDataPort).delete(subscription);
    }

    @Test
    void execute_shouldThrowBadRequest_whenSubscriptionDoesNotExist() {
        User user = new User("john@test.com", "john");
        Long userId = user.getId();

        Subject subject = new Subject("Java", "Java programming");
        Long subjectId = subject.getId();

        UnsubscribeRequest request = new UnsubscribeRequest(subjectId, userId);

        when(subscriptionDataPort.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.execute(request));

        assertEquals("L'utilisateur n'est pas abonné à ce sujet.", exception.getMessage());
        verify(subscriptionDataPort, never()).delete(any());
    }
}
