package com.openclassrooms.mddapi.infrastructure.user.authorization;

import com.openclassrooms.mddapi.infrastructure.user.authorization.UserAuthorization;
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
class UserAuthorizationIntegrationTest {

    @Autowired
    private UserAuthorization userAuthorization;

    @MockBean
    private AuthorizationService authorizationService;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
    }

    @Test
    void canGet_shouldCallAuthorizeWithReadAction() {
        boolean result = userAuthorization.canGet(userId);

        assertTrue(result);

        verify(authorizationService, times(1))
            .authorize(argThat(arg -> {
                AuthorizationRequest req = (AuthorizationRequest) arg;
                return req.ownerId().equals(userId)
                    && "user".equals(req.resourceName());
            }), eq(Action.READ)
        );
    }

    @Test
    void canUpdate_shouldCallAuthorizeWithSubscribeAction() {
        boolean result = userAuthorization.canUpdate(userId);

        assertTrue(result);

        verify(authorizationService, times(1))
            .authorize(argThat(arg -> {
                AuthorizationRequest req = (AuthorizationRequest) arg;
                return req.ownerId().equals(userId)
                    && "user".equals(req.resourceName());
            }), eq(Action.SUBSCRIBE)
        );
    }

    @Test
    void authorizationServiceThrowsException_shouldPropagate() {
        doThrow(new RuntimeException("Not authorized"))
            .when(authorizationService)
            .authorize(any(AuthorizationRequest.class), any(Action.class));

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userAuthorization.canGet(userId));

        assertEquals("Not authorized", exception.getMessage());
    }
}
