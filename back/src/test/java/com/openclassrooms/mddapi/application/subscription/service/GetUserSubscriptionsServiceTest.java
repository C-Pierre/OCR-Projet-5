package com.openclassrooms.mddapi.application.subscription.service;

import java.util.List;
import java.util.Arrays;

import com.openclassrooms.mddapi.application.subscription.service.GetUserSubscriptionsService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.subject.entity.Subject;
import com.openclassrooms.mddapi.application.subject.dto.SubjectDto;
import com.openclassrooms.mddapi.domain.subscription.entity.Subscription;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.subscription.repository.port.SubscriptionDataPort;

class GetUserSubscriptionsServiceTest {

    private GetUserSubscriptionsService service;
    private SubscriptionDataPort subscriptionDataPort;
    private UserDataPort userDataPort;

    @BeforeEach
    void setUp() {
        subscriptionDataPort = mock(SubscriptionDataPort.class);
        userDataPort = mock(UserDataPort.class);

        service = new GetUserSubscriptionsService(subscriptionDataPort, userDataPort);
    }

    @Test
    void execute_shouldReturnListOfSubjectDto() {
        User user = new User("john@test.com", "john");
        Long userId = user.getId();

        Subject subject1 = new Subject("Java", "Description");
        subject1.setDescription("Java programming");

        Subject subject2 = new Subject("Spring", "Description");
        subject2.setDescription("Spring Framework");

        Subscription sub1 = new Subscription(user, subject1);
        Subscription sub2 = new Subscription(user, subject2);

        when(userDataPort.getById(userId)).thenReturn(user);
        when(subscriptionDataPort.findByUserId(userId)).thenReturn(Arrays.asList(sub1, sub2));

        List<SubjectDto> result = service.execute(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        SubjectDto dto1 = result.getFirst();
        assertEquals("Java", dto1.name());
        assertEquals("Java programming", dto1.description());

        SubjectDto dto2 = result.get(1);
        assertEquals("Spring", dto2.name());
        assertEquals("Spring Framework", dto2.description());

        verify(userDataPort).getById(userId);
        verify(subscriptionDataPort).findByUserId(userId);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoSubscriptions() {
        User user = new User("alice@test.com", "Alice");
        Long userId = user.getId();

        when(userDataPort.getById(userId)).thenReturn(user);
        when(subscriptionDataPort.findByUserId(userId)).thenReturn(List.of());

        List<SubjectDto> result = service.execute(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userDataPort).getById(userId);
        verify(subscriptionDataPort).findByUserId(userId);
    }

    @Test
    void execute_shouldThrowException_whenUserNotFound() {
        Long userId = 3L;

        when(userDataPort.getById(userId)).thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> service.execute(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userDataPort).getById(userId);
        verifyNoInteractions(subscriptionDataPort);
    }
}
