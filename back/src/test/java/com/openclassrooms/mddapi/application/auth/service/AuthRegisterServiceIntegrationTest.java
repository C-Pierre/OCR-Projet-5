package com.openclassrooms.mddapi.application.auth.service;

import com.openclassrooms.mddapi.application.auth.service.AuthRegisterService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.api.auth.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.api.common.response.MessageResponse;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.BadRequestException;

@SpringBootTest
@ActiveProfiles("test")
class AuthRegisterServiceIntegrationTest {

    @Autowired
    private AuthRegisterService authRegisterService;

    @Autowired
    private UserDataPort userDataPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testExecute_Integration_Success() {
        RegisterRequest request = new RegisterRequest(
            "UserTest", "user.test@mail.com", "test!1234"
        );

        MessageResponse response = authRegisterService.execute(request);

        assertNotNull(response);
        assertEquals("User registered successfully!", response.message());

        User saved = userDataPort.getByEmail("user.test@mail.com");
        assertNotNull(saved);
        assertEquals("UserTest", saved.getUserName());
        assertTrue(passwordEncoder.matches("test!1234", saved.getPassword()));
    }

    @Test
    void testExecute_Integration_EmailAlreadyTaken() {
        String email = "user.one@mail.com";
        String username = "existingUser";

        User existing = null;
        try {
            existing = userDataPort.getByEmail(email);
        } catch (Exception ignored) {}

        if (existing == null) {
            existing = new User(email, username);
            existing.setPassword(passwordEncoder.encode("test!1234"));
            userRepository.saveAndFlush(existing);
        }

        RegisterRequest request = new RegisterRequest(
            "newUser", email, "test!1234"
        );

        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> authRegisterService.execute(request));

        assertEquals("Error: Email is already taken!", exception.getMessage());
    }

    @Test
    void testExecute_Integration_UserNameAlreadyTaken() {
        String email = "unique" + System.currentTimeMillis() + "@mail.com";
        String username = "UserOne";

        User existing = null;
        try {
            existing = userDataPort.getByUserName(username);
        } catch (Exception ignored) {}

        if (existing == null) {
            existing = new User(email, username);
            existing.setPassword(passwordEncoder.encode("pass"));
            userRepository.saveAndFlush(existing);
        }

        RegisterRequest request = new RegisterRequest(
            username, "newuser" + System.currentTimeMillis() + "@mail.com", "password123"
        );

        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> authRegisterService.execute(request));

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }
}
