package com.openclassrooms.mddapi.domain.user.entity;

import java.util.Set;

import com.openclassrooms.mddapi.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserTest {

    private static final Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void user_withValidFields_shouldPassValidation() {
        User user = new User("test@mail.com", "testuser");
        user.setPassword("securePassword123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    void user_withBlankFields_shouldFailValidation() {
        User user = new User("", "");
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(3);
    }

    @Test
    void user_withInvalidEmail_shouldFailValidation() {
        User user = new User("not-an-email", "username");
        user.setPassword("password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
    }

    @Test
    void saveUser_shouldPersistData() {
        User user = new User("persist@mail.com", "persistUser");
        user.setPassword("password123");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("persist@mail.com");
        assertThat(saved.getUserName()).isEqualTo("persistUser");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void saveUser_withDuplicateEmail_shouldThrowException() {
        User user1 = new User("dup@mail.com", "user1");
        user1.setPassword("password123");
        userRepository.save(user1);

        User user2 = new User("dup@mail.com", "user2");
        user2.setPassword("password456");

        assertThrows(Exception.class, () -> userRepository.saveAndFlush(user2));
    }

    @Test
    void setUserName_shouldUpdateValue() {
        User user = new User("email@mail.com", "initialName");
        user.setPassword("password123");

        user.setUserName("newName");

        assertThat(user.getUserName()).isEqualTo("newName");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    void setEmail_shouldUpdateValueAndValidate() {
        User user = new User("old@mail.com", "username");
        user.setPassword("password123");

        user.setEmail("new@mail.com");
        assertThat(user.getEmail()).isEqualTo("new@mail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();

        user.setEmail("invalid-email");
        violations = validator.validate(user);
        assertThat(violations).hasSize(1);
    }
}
