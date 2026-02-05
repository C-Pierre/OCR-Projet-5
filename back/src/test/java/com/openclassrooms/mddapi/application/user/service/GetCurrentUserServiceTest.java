package com.openclassrooms.mddapi.application.user.service;

import com.openclassrooms.mddapi.application.user.service.GetCurrentUserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.Authentication;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.openclassrooms.mddapi.infrastructure.user.mapper.UserMapper;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.UnauthorizedException;

class GetCurrentUserServiceTest {

    private GetCurrentUserService service;
    private UserDataPort userDataPort;
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        userDataPort = mock(UserDataPort.class);
        mapper = mock(UserMapper.class);
        service = new GetCurrentUserService(userDataPort, mapper);
    }

    @Test
    void execute_shouldReturnUserDto_whenAuthenticated() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("john");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        User user = new User( "john@test.com", "john");

        when(userDataPort.getByUserName("john")).thenReturn(user);

        UserDto dto = new UserDto(1L, "john", "john@test.com", null, null);
        when(mapper.toDto(user)).thenReturn(dto);

        UserDto result = service.execute();

        assertNotNull(result);
        assertEquals("john", result.userName());
        assertEquals("john@test.com", result.email());

        verify(userDataPort).getByUserName("john");
        verify(mapper).toDto(user);
    }

    @Test
    void execute_shouldThrowUnauthorizedException_whenNotAuthenticated() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> service.execute()
        );

        assertEquals("Vous n'êtes pas authentifié.", exception.getMessage());
    }

    @Test
    void execute_shouldThrowUnauthorizedException_whenNotAuthenticatedFlagFalse() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> service.execute()
        );

        assertEquals("Vous n'êtes pas authentifié.", exception.getMessage());
    }
}
