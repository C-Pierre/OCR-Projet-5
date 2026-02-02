package com.openclassrooms.mddapi.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.common.response.MessageResponse;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.common.exception.type.BadRequestException;

class AuthRegisterServiceTest {

    @InjectMocks
    private AuthRegisterService authRegisterService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDataPort userDataPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_Success() {
        RegisterRequest request = new RegisterRequest(
            "testUser", "test@example.com", "password123"
        );

        when(userDataPort.existsByEmail("test@example.com")).thenReturn(false);
        when(userDataPort.existsByUserName("testUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        MessageResponse response = authRegisterService.execute(request);

        assertNotNull(response);
        assertEquals("User registered successfully!", response.message());

        verify(userDataPort).existsByEmail("test@example.com");
        verify(userDataPort).existsByUserName("testUser");
        verify(passwordEncoder).encode("password123");
        verify(userDataPort).save(any(User.class));
    }

    @Test
    void testExecute_EmailAlreadyTaken_ShouldThrow() {
        RegisterRequest request = new RegisterRequest(
                "UserOne", "user.one@mail.com", "password123"
        );

        when(userDataPort.existsByEmail("user.one@mail.com")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authRegisterService.execute(request));

        assertEquals("Error: Email is already taken!", exception.getMessage());
    }

    @Test
    void testExecute_UserNameAlreadyTaken_ShouldThrow() {
        RegisterRequest request = new RegisterRequest(
                "UserOne", "test@example.com", "password123"
        );

        when(userDataPort.existsByEmail("test@example.com")).thenReturn(false);
        when(userDataPort.existsByUserName("UserOne")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authRegisterService.execute(request));

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }
}
