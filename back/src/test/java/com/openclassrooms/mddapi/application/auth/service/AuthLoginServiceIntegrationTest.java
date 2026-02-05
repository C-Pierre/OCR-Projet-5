package com.openclassrooms.mddapi.application.auth.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.api.auth.response.JwtResponse;
import com.openclassrooms.mddapi.api.auth.request.LoginRequest;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

@SpringBootTest
@ActiveProfiles("test")
class AuthLoginServiceIntegrationTest {

    @Autowired
    private AuthLoginService authLoginService;

    @Test
    void testExecute_Integration_Success() {
        LoginRequest loginRequest = new LoginRequest("user.one@mail.com", "test!1234");

        JwtResponse response = authLoginService.execute(loginRequest);

        assertNotNull(response);
        assertNotNull(response.token());
        assertEquals("user.one@mail.com", response.email());
    }

    @Test
    void testExecute_Integration_Failure() {
        LoginRequest loginRequest = new LoginRequest("nonexistent@mail.com", "wrongPassword");

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> authLoginService.execute(loginRequest)
        );

        assertEquals("User not found with email: nonexistent@mail.com", exception.getMessage());
    }
}
