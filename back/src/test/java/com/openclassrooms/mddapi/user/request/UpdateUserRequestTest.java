package com.openclassrooms.mddapi.user.request;

import java.util.Set;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeAll;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateUserRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_whenAllFieldsAreValid() {
        UpdateUserRequest request = new UpdateUserRequest(
            "john",
            "john@test.com",
            "!Test1234"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations =
            validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFail_whenEmailIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest(
            "john",
            "invalid-email",
            "!Test1234"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations =
            validator.validate(request);

        assertThat(violations)
            .anyMatch(v ->
                v.getPropertyPath().toString().equals("email") &&
                    v.getMessage().equals("Email format is invalid")
            );
    }

    @Test
    void shouldFail_whenPasswordTooShort() {
        UpdateUserRequest request = new UpdateUserRequest(
            "john",
            "john@test.com",
            "123"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations =
            validator.validate(request);

        assertThat(violations)
            .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFail_whenUserNameTooLong() {
        String longName = "a".repeat(251);

        UpdateUserRequest request = new UpdateUserRequest(
            longName,
            "john@test.com",
            "!Test1234"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations =
            validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Username must be 250 characters max");
    }
}
