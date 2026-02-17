package com.openclassrooms.mddapi.domain.user.repository;

import java.util.Optional;

import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john@test.com", "john");
        user.setPassword("secret123");
        userRepository.save(user);
    }

    @Test
    void existsByUserName_shouldReturnTrue_whenUserExists() {
        boolean exists = userRepository.existsByUserName("john");
        assertTrue(exists);
    }

    @Test
    void existsByUserName_shouldReturnFalse_whenUserDoesNotExist() {
        boolean exists = userRepository.existsByUserName("nonexistent");
        assertFalse(exists);
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        boolean exists = userRepository.existsByEmail("john@test.com");
        assertTrue(exists);
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("noone@test.com");
        assertFalse(exists);
    }

    @Test
    void findByEmail_shouldReturnUser_whenExists() {
        Optional<User> found = userRepository.findByEmail("john@test.com");
        assertTrue(found.isPresent());
        assertEquals("john", found.get().getUserName());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenNotExists() {
        Optional<User> found = userRepository.findByEmail("noone@test.com");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByUserName_shouldReturnUser_whenExists() {
        Optional<User> found = userRepository.findByUserName("john");
        assertTrue(found.isPresent());
        assertEquals("john@test.com", found.get().getEmail());
    }

    @Test
    void findByUserName_shouldReturnEmpty_whenNotExists() {
        Optional<User> found = userRepository.findByUserName("nonexistent");
        assertTrue(found.isEmpty());
    }

    @Test
    void save_shouldPersistUser() {
        User newUser = new User("alice@test.com", "alice");
        newUser.setPassword("password123");

        User saved = userRepository.save(newUser);

        assertNotNull(saved.getId());
        assertEquals("alice", saved.getUserName());
        assertEquals("alice@test.com", saved.getEmail());
    }

    @Test
    void update_shouldModifyUser() {
        user.setUserName("johnny");
        userRepository.save(user);

        Optional<User> updated = userRepository.findByUserName("johnny");
        assertTrue(updated.isPresent());
        assertEquals("john@test.com", updated.get().getEmail());
    }
}
