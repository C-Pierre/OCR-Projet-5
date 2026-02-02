package com.openclassrooms.mddapi.auth.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.auth.request.LoginRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.auth.jwt.response.JwtResponse;
import com.openclassrooms.mddapi.common.exception.type.NotFoundException;
import org.springframework.test.context.ActiveProfiles;

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
