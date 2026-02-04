package com.openclassrooms.mddapi.auth.validator;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ConstraintValidatorContext;
import static org.assertj.core.api.Assertions.assertThat;

class AuthPasswordValidatorTest {

    private AuthPasswordValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new AuthPasswordValidator();

        context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder = Mockito.mock(
            ConstraintValidatorContext.ConstraintViolationBuilder.class
        );

        when(context.buildConstraintViolationWithTemplate(anyString()))
            .thenReturn(violationBuilder);

        when(violationBuilder.addConstraintViolation())
            .thenReturn(context);
    }

    @Test
    void shouldReturnFalse_whenPasswordIsNull() {
        boolean result = validator.isValid(null, context);

        assertThat(result).isFalse();
        verifyNoInteractions(context);
    }

    @Test
    void shouldReturnFalse_whenPasswordIsBlank() {
        boolean result = validator.isValid("   ", context);

        assertThat(result).isFalse();
        verifyNoInteractions(context);
    }

    @Test
    void shouldFail_whenPasswordIsTooShort() {
        boolean result = validator.isValid("Aa1!", context);

        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must be between 8 and 250 characters"
        );
    }

    @Test
    void shouldFail_whenPasswordIsTooLong() {
        String longPassword =
                "A1!".repeat(84) + "A";

        boolean result = validator.isValid(longPassword, context);

        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must be between 8 and 250 characters"
        );
    }

    @Test
    void shouldFail_whenMissingUppercase() {
        boolean result = validator.isValid("password1!", context);

        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must contain at least one uppercase letter"
        );
    }

    @Test
    void shouldFail_whenMissingLowercase() {
        boolean result = validator.isValid("PASSWORD1!", context);

        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must contain at least one lowercase letter"
        );
    }

    @Test
    void shouldFail_whenMissingDigit() {
        boolean result = validator.isValid("Password!", context);

        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must contain at least one digit"
        );
    }

    @Test
    void shouldFail_whenMissingSpecialCharacter() {
        boolean result = validator.isValid("Password1", context);

        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
            "Password must contain at least one special character"
        );
    }

    @Test
    void shouldCollectMultipleViolations_whenPasswordIsVeryWeak() {
        boolean result = validator.isValid("password", context);

        assertThat(result).isFalse();

        verify(context).disableDefaultConstraintViolation();
        verify(context, atLeastOnce()).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    void shouldReturnTrue_whenPasswordIsValid() {
        boolean result = validator.isValid("StrongPass1!", context);

        assertThat(result).isTrue();
        verify(context).disableDefaultConstraintViolation();
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
    }
}
