package com.openclassrooms.mddapi.application.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.openclassrooms.mddapi.application.auth.service.AuthLoginService;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsImpl;
import com.openclassrooms.mddapi.infrastructure.auth.jwt.JwtUtils;
import com.openclassrooms.mddapi.api.auth.response.JwtResponse;
import com.openclassrooms.mddapi.api.auth.request.LoginRequest;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.BadRequestException;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthLoginServiceTest {

    @InjectMocks
    private AuthLoginService authLoginService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDataPort userDataPort;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testExecute_WithEmail_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        User user = new User("test@example.com", "testUser");
        user.setPassword("password");

        when(userDataPort.getByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getEmail()).thenReturn("test@example.com");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        // Act
        JwtResponse response = authLoginService.execute(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals(userDetails.getId(), response.id());
        assertEquals(userDetails.getUsername(), response.username());
        assertEquals(userDetails.getEmail(), response.email());

        verify(userDataPort).getByEmail("test@example.com");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
    }

    @Test
    void testExecute_WithUserName_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "password");

        User user = new User("test@example.com", "testUser");
        user.setPassword("password");

        when(userDataPort.getByUserName("testUser")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(2L);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getEmail()).thenReturn("test@example.com");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token-2");

        // Act
        JwtResponse response = authLoginService.execute(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-2", response.token());
        assertEquals(userDetails.getId(), response.id());
        assertEquals(userDetails.getUsername(), response.username());
        assertEquals(userDetails.getEmail(), response.email());

        verify(userDataPort).getByUserName("testUser");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
    }

    @Test
    void testExecute_UserNotFound_ShouldThrowException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("unknown@example.com", "password");

        when(userDataPort.getByEmail("unknown@example.com")).thenReturn(null);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authLoginService.execute(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testExecute_AuthenticationFails_ShouldThrowException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrong");

        User user = new User("test@example.com", "testUser");
        user.setPassword("wrong");

        when(userDataPort.getByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authLoginService.execute(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }
}
