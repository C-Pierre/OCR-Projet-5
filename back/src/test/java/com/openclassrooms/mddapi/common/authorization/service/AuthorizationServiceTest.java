package com.openclassrooms.mddapi.common.authorization.service;

import com.openclassrooms.mddapi.common.authorization.Action;
import com.openclassrooms.mddapi.common.authorization.policy.AuthorizationPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private AuthorizationPolicy policy1;
    private AuthorizationPolicy policy2;
    private AuthorizationService authorizationService;

    private final Object resource = new Object();

    @BeforeEach
    void setUp() {
        policy1 = mock(AuthorizationPolicy.class);
        policy2 = mock(AuthorizationPolicy.class);
    }

    @Test
    void authorize_shouldCallCheckOnMatchingPolicy() {
        when(policy1.supports(Action.READ, resource)).thenReturn(true);

        authorizationService = new AuthorizationService(List.of(policy1, policy2));

        authorizationService.authorize(resource, Action.READ);

        verify(policy1).check(resource);
        verify(policy1).supports(Action.READ, resource);
        verifyNoInteractions(policy2);
    }

    @Test
    void authorize_shouldUseFirstMatchingPolicyOnly() {
        when(policy1.supports(Action.UPDATE, resource)).thenReturn(true);
        when(policy2.supports(Action.UPDATE, resource)).thenReturn(true);

        authorizationService = new AuthorizationService(List.of(policy1, policy2));

        authorizationService.authorize(resource, Action.UPDATE);

        verify(policy1).check(resource);
        verify(policy2, never()).check(any());
    }

    @Test
    void authorize_shouldThrowException_whenNoPolicySupportsAction() {
        when(policy1.supports(Action.DELETE, resource)).thenReturn(false);
        when(policy2.supports(Action.DELETE, resource)).thenReturn(false);

        authorizationService = new AuthorizationService(List.of(policy1, policy2));

        assertThatThrownBy(() ->
                authorizationService.authorize(resource, Action.DELETE)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No policy for DELETE");

        verify(policy1).supports(Action.DELETE, resource);
        verify(policy2).supports(Action.DELETE, resource);
        verify(policy1, never()).check(any());
        verify(policy2, never()).check(any());
    }
}
