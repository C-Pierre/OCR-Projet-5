package com.openclassrooms.mddapi.application.auth.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsImpl;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsServiceImpl;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

@SpringBootTest
@ActiveProfiles("test")
class UserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserDataPort userDataPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loadUserById_Success() {
        User user = new User("integration@mail.com", "IntegrationUser");
        user.setPassword(passwordEncoder.encode("pass123"));
        userDataPort.save(user);

        UserDetails userDetails = userDetailsService.loadUserById(user.getId());

        assertNotNull(userDetails);
        assertEquals("IntegrationUser", userDetails.getUsername());
        assertEquals("integration@mail.com", ((UserDetailsImpl) userDetails).getEmail());
        assertTrue(passwordEncoder.matches("pass123", userDetails.getPassword()));
    }

    @Test
    void loadUserById_NotFound_ShouldThrow() {
        Long fakeId = 999999L;

        NotFoundException ex = assertThrows(
            NotFoundException.class,
            () -> userDetailsService.loadUserById(fakeId)
        );

        assertEquals("User not found with id: " + fakeId, ex.getMessage());
    }

    @Test
    void loadUserByUsername_Success() {
        User user = new User("integration2@mail.com", "IntegrationUser2");
        user.setPassword(passwordEncoder.encode("pass456"));
        userDataPort.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("IntegrationUser2");

        assertNotNull(userDetails);
        assertEquals("IntegrationUser2", userDetails.getUsername());
        assertEquals("integration2@mail.com", ((UserDetailsImpl) userDetails).getEmail());
        assertTrue(passwordEncoder.matches("pass456", userDetails.getPassword()));
    }

    @Test
    void loadUserByUsername_NotFound_ShouldThrow() {
        String fakeUsername = "unknownUser";

        NotFoundException ex = assertThrows(
            NotFoundException.class,
            () -> userDetailsService.loadUserByUsername(fakeUsername)
        );

        assertEquals("User not found with username: " + fakeUsername, ex.getMessage());
    }
}
