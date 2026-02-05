package com.openclassrooms.mddapi.api.post.request;

import java.util.Set;

import com.openclassrooms.mddapi.api.post.request.CreatePostRequest;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;

class CreatePostRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        CreatePostRequest request = new CreatePostRequest(
            "Valid Post Title",
            "This is a valid post content",
            1L,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void titleBlank_shouldFailNotBlankValidation() {
        CreatePostRequest request = new CreatePostRequest(
            "",
            "Content is valid",
            1L,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Title must not be blank");
    }

    @Test
    void titleTooLong_shouldFailSizeValidation() {
        String longTitle = "A".repeat(201);
        CreatePostRequest request = new CreatePostRequest(
            longTitle,
            "Valid content",
            1L,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Title must be 200 characters max");
    }

    @Test
    void contentBlank_shouldFailNotBlankValidation() {
        CreatePostRequest request = new CreatePostRequest(
            "Valid title",
            "",
            1L,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Content must not be blank");
    }

    @Test
    void contentTooLong_shouldFailSizeValidation() {
        String longContent = "A".repeat(5001);
        CreatePostRequest request = new CreatePostRequest(
            "Valid title",
            longContent,
            1L,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Content must be 5000 characters max");
    }

    @Test
    void subjectIdNull_shouldFailNotNullValidation() {
        CreatePostRequest request = new CreatePostRequest(
            "Valid title",
            "Valid content",
            null,
            2L
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Subject ID must not be null");
    }

    @Test
    void authorIdNull_shouldFailNotNullValidation() {
        CreatePostRequest request = new CreatePostRequest(
            "Valid title",
            "Valid content",
            1L,
            null
        );

        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(request);

        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Author ID must not be null");
    }
}
