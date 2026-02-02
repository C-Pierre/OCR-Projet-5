package com.openclassrooms.mddapi.subscription.service;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.subject.entity.Subject;
import com.openclassrooms.mddapi.subscription.entity.Subscription;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.subscription.request.SubscribeRequest;
import com.openclassrooms.mddapi.subject.repository.port.SubjectDataPort;
import com.openclassrooms.mddapi.common.exception.type.BadRequestException;
import com.openclassrooms.mddapi.subscription.repository.port.SubscriptionDataPort;

class SubscribeServiceTest {

    private SubscriptionDataPort subscriptionDataPort;
    private SubjectDataPort subjectDataPort;
    private UserDataPort userDataPort;
    private SubscribeService service;

    @BeforeEach
    void setUp() {
        subscriptionDataPort = mock(SubscriptionDataPort.class);
        subjectDataPort = mock(SubjectDataPort.class);
        userDataPort = mock(UserDataPort.class);

        service = new SubscribeService(subscriptionDataPort, subjectDataPort, userDataPort);
    }

    @Test
    void execute_shouldSaveSubscription_whenNotAlreadySubscribed() {
        User user = new User("john@test.com", "john");
        Long userId = user.getId();

        Subject subject = new Subject("Java", "Java programming");
        Long subjectId = subject.getId();

        SubscribeRequest request = new SubscribeRequest(subjectId, userId);

        when(userDataPort.getById(userId)).thenReturn(user);
        when(subjectDataPort.getById(subjectId)).thenReturn(subject);
        when(subscriptionDataPort.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.empty());

        service.execute(request);

        verify(subscriptionDataPort).save(any(Subscription.class));
    }

    @Test
    void execute_shouldThrowBadRequest_whenAlreadySubscribed() {
        User user = new User("john@test.com", "john");
        Long userId = user.getId();

        Subject subject = new Subject("Java", "Java programming");
        Long subjectId = subject.getId();

        SubscribeRequest request = new SubscribeRequest(subjectId, userId);

        Subscription existingSub = new Subscription(user, subject);

        when(userDataPort.getById(userId)).thenReturn(user);
        when(subjectDataPort.getById(subjectId)).thenReturn(subject);
        when(subscriptionDataPort.findByUserIdAndSubjectId(userId, subjectId))
                .thenReturn(Optional.of(existingSub));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.execute(request));

        assertEquals("L'utilisateur est déjà abonné à ce sujet.", exception.getMessage());
        verify(subscriptionDataPort, never()).save(any());
    }
}
