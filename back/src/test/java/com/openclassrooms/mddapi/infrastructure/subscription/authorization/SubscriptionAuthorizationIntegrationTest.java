package com.openclassrooms.mddapi.infrastructure.subscription.authorization;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.infrastructure.common.authorization.service.AuthorizationService;
import com.openclassrooms.mddapi.infrastructure.common.authorization.request.AuthorizationRequest;

@SpringBootTest
class SubscriptionAuthorizationIntegrationTest {

    @Autowired
    private SubscriptionAuthorization subscriptionAuthorization;

    @MockBean
    private AuthorizationService authorizationService;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
    }

    @Test
    void canGet_shouldCallAuthorizeWithReadAction() {
        boolean result = subscriptionAuthorization.canGet(userId);

        assertTrue(result);

        verify(authorizationService, times(1))
                .authorize(argThat(arg -> {
                    AuthorizationRequest req = (AuthorizationRequest) arg;
                    return req.ownerId().equals(userId)
                            && "subscription".equals(req.resourceName());
                }), eq(Action.READ));
    }

    @Test
    void canSubscribe_shouldCallAuthorizeWithSubscribeAction() {
        boolean result = subscriptionAuthorization.canSubscribe(userId);

        assertTrue(result);

        verify(authorizationService, times(1))
                .authorize(argThat(arg -> {
                    AuthorizationRequest req = (AuthorizationRequest) arg;
                    return req.ownerId().equals(userId)
                            && "subscription".equals(req.resourceName());
                }), eq(Action.SUBSCRIBE));
    }

    @Test
    void canUnsubscribe_shouldCallAuthorizeWithUnsubscribeAction() {
        boolean result = subscriptionAuthorization.canUnsubscribe(userId);

        assertTrue(result);

        verify(authorizationService, times(1))
                .authorize(argThat(arg -> {
                    AuthorizationRequest req = (AuthorizationRequest) arg;
                    return req.ownerId().equals(userId)
                            && "subscription".equals(req.resourceName());
                }), eq(Action.UNSUBSCRIBE));
    }

    @Test
    void authorizationServiceThrowsException_shouldPropagate() {
        doThrow(new RuntimeException("Not authorized"))
                .when(authorizationService)
                .authorize(any(AuthorizationRequest.class), any(Action.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subscriptionAuthorization.canGet(userId)
        );

        assertEquals("Not authorized", exception.getMessage());
    }
}
