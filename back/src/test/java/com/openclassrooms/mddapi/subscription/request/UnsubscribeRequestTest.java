package com.openclassrooms.mddapi.subscription.request;

import java.util.Set;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeAll;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;

class UnsubscribeRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_whenAllFieldsAreValid() {
        UnsubscribeRequest request = new UnsubscribeRequest(1L, 2L);

        Set<ConstraintViolation<UnsubscribeRequest>> violations =
            validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFail_whenSubjectIdIsNull() {
        UnsubscribeRequest request = new UnsubscribeRequest(null, 2L);

        Set<ConstraintViolation<UnsubscribeRequest>> violations =
            validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Subject ID must not be null");
    }

    @Test
    void shouldFail_whenUserIdIsNull() {
        UnsubscribeRequest request = new UnsubscribeRequest(1L, null);

        Set<ConstraintViolation<UnsubscribeRequest>> violations =
            validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("User ID must not be null");
    }
}
