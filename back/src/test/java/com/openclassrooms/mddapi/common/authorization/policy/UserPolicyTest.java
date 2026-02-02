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

class UserPolicyTest {

    private GetCurrentUserService getCurrentUserService;
    private UserShipPolicy policy;

    @BeforeEach
    void setUp() {
        getCurrentUserService = mock(GetCurrentUserService.class);
        policy = new UserShipPolicy(getCurrentUserService);
    }

    @Test
    void supports_shouldReturnTrue_forReadUpdateDelete() {
        Ownable resource = mock(Ownable.class);

        assertThat(policy.supports(Action.READ, resource)).isTrue();
        assertThat(policy.supports(Action.UPDATE, resource)).isTrue();
        assertThat(policy.supports(Action.DELETE, resource)).isTrue();
    }

    @Test
    void supports_shouldReturnFalse_forOtherActions() {
        Ownable resource = mock(Ownable.class);

        assertThat(policy.supports(Action.SUBSCRIBE, resource)).isFalse();
    }

    @Test
    void check_shouldPass_whenUserIsOwner() {
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
    }

    @Test
    void check_shouldThrowForbiddenException_whenUserIsNotOwner() {
        Ownable resource = mock(Ownable.class);
        when(resource.ownerId()).thenReturn(20L);
        when(resource.resourceName()).thenReturn("user");

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
            .hasMessage("Vous ne pouvez pas éditer cette ressource user");
    }
}
