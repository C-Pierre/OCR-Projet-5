package com.openclassrooms.mddapi.comment.request;

import java.util.Set;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;

class CreateCommentRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        CreateCommentRequest request = new CreateCommentRequest(
            "This is a valid comment",
            1L,
            2L
        );

        Set<ConstraintViolation<CreateCommentRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void contentBlank_shouldFailNotBlankValidation() {
        CreateCommentRequest request = new CreateCommentRequest(
            "",
            1L,
            2L
        );

        Set<ConstraintViolation<CreateCommentRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Content must not be blank");
    }

    @Test
    void contentTooLong_shouldFailSizeValidation() {
        String longContent = "A".repeat(2501);
        CreateCommentRequest request = new CreateCommentRequest(
            longContent,
            1L,
            2L
        );

        Set<ConstraintViolation<CreateCommentRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Content must be 2500 characters max");
    }

    @Test
    void postIdNull_shouldFailNotNullValidation() {
        CreateCommentRequest request = new CreateCommentRequest(
            "Valid content",
            null,
            2L
        );

        Set<ConstraintViolation<CreateCommentRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("PostId must not be blank");
    }

    @Test
    void userIdNull_shouldFailNotNullValidation() {
        CreateCommentRequest request = new CreateCommentRequest(
            "Valid content",
            1L,
            null
        );

        Set<ConstraintViolation<CreateCommentRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("UserId must not be blank");
    }
}
