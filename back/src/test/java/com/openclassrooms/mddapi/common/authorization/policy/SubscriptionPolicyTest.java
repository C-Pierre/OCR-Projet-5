package com.openclassrooms.mddapi.common.authorization.policy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;
import com.openclassrooms.mddapi.user.dto.UserDto;
import com.openclassrooms.mddapi.common.authorization.Action;
import com.openclassrooms.mddapi.common.authorization.Ownable;
import com.openclassrooms.mddapi.user.service.GetCurrentUserService;
import com.openclassrooms.mddapi.common.exception.type.ForbiddenException;

class SubscriptionPolicyTest {

    private GetCurrentUserService getCurrentUserService;
    private SubscriptionPolicy policy;

    @BeforeEach
    void setUp() {
        getCurrentUserService = mock(GetCurrentUserService.class);
        policy = new SubscriptionPolicy(getCurrentUserService);
    }

    @Test
    void supports_shouldReturnTrue_forSubscribeAction() {
        Ownable resource = mock(Ownable.class);

        boolean result = policy.supports(Action.SUBSCRIBE, resource);

        assertThat(result).isTrue();
    }

    @Test
    void supports_shouldReturnTrue_forUnsubscribeAction() {
        Ownable resource = mock(Ownable.class);

        boolean result = policy.supports(Action.UNSUBSCRIBE, resource);

        assertThat(result).isTrue();
    }

    @Test
    void supports_shouldReturnFalse_forOtherActions() {
        Ownable resource = mock(Ownable.class);

        boolean result = policy.supports(Action.UPDATE, resource);

        assertThat(result).isFalse();
    }

    @Test
    void check_shouldPass_whenCurrentUserIsOwner() {
        Ownable resource = mock(Ownable.class);
        when(resource.ownerId()).thenReturn(1L);

        when(getCurrentUserService.execute())
            .thenReturn(new UserDto(
                1L,
                "user",
                "user@test.com",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );

        policy.check(resource);

        verify(resource).ownerId();
        verify(getCurrentUserService).execute();
    }

    @Test
    void check_shouldThrowForbiddenException_whenUserIsNotOwner() {
        Ownable resource = mock(Ownable.class);
        when(resource.ownerId()).thenReturn(2L);
        when(resource.resourceName()).thenReturn("subscription");

        when(getCurrentUserService.execute())
            .thenReturn(new UserDto(
                1L,
                "user",
                "user@test.com",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );

        assertThatThrownBy(() -> policy.check(resource))
            .isInstanceOf(ForbiddenException.class)
            .hasMessage("Vous ne pouvez pas éditer cette ressource subscription");
    }
}
