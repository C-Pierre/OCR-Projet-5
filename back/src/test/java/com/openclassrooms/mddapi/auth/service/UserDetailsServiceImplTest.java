package com.openclassrooms.mddapi.auth.service;

import org.mockito.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDataPort userDataPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserById_Success() {
        User user = new User("test@mail.com", "testUser");
        user.setPassword("password");

        when(userDataPort.getById(1L)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserById(1L);

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("test@mail.com", ((UserDetailsImpl)userDetails).getEmail());
    }

    @Test
    void loadUserById_NotFound_ShouldThrow() {
        when(userDataPort.getById(99L)).thenReturn(null);

        UsernameNotFoundException ex = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserById(99L)
        );

        assertEquals("Utilisateur non trouvé avec le id : 99", ex.getMessage());
    }

    @Test
    void loadUserByUsername_Success() {
        User user = new User("test@mail.com", "testUser");
        user.setPassword("password");

        when(userDataPort.getByUserName("testUser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("test@mail.com", ((UserDetailsImpl)userDetails).getEmail());
    }

    @Test
    void loadUserByUsername_NotFound_ShouldThrow() {
        when(userDataPort.getByUserName("unknown")).thenReturn(null);

        UsernameNotFoundException ex = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("unknown")
        );

        assertEquals("Utilisateur non trouvé avec le username : unknown", ex.getMessage());
    }
}
