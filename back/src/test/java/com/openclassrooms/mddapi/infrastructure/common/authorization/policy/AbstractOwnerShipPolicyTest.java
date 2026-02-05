package com.openclassrooms.mddapi.infrastructure.common.authorization.policy;

import java.util.EnumSet;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Action;
import com.openclassrooms.mddapi.infrastructure.common.authorization.Ownable;
import com.openclassrooms.mddapi.application.user.service.GetCurrentUserService;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.ForbiddenException;

class AbstractOwnerShipPolicyTest {

    private GetCurrentUserService getCurrentUserService;
    private AbstractOwnerShipPolicy policy;

    @BeforeEach
    void setUp() {
        getCurrentUserService = mock(GetCurrentUserService.class);

        policy = new AbstractOwnerShipPolicy(getCurrentUserService) {
            @Override
            protected EnumSet<Action> supportedActions() {
                return EnumSet.of(Action.UPDATE, Action.DELETE);
            }

            @Override
            protected String errorMessage(Ownable resource) {
                return "Not owner";
            }
        };
    }

    @Test
    void supports_shouldReturnTrue_whenActionSupportedAndResourceOwnable() {
        Ownable resource = mock(Ownable.class);

        boolean result = policy.supports(Action.UPDATE, resource);

        assertThat(result).isTrue();
    }

    @Test
    void supports_shouldReturnFalse_whenActionNotSupported() {
        Ownable resource = mock(Ownable.class);

        boolean result = policy.supports(Action.READ, resource);

        assertThat(result).isFalse();
    }

    @Test
    void supports_shouldReturnFalse_whenResourceIsNotOwnable() {
        Object resource = new Object();

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
    void check_shouldThrowForbiddenException_whenCurrentUserIsNotOwner() {
        Ownable resource = mock(Ownable.class);
        when(resource.ownerId()).thenReturn(2L);

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
            .hasMessage("Not owner");
    }
}
