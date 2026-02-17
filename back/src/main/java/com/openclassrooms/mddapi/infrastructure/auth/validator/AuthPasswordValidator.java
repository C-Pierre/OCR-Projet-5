package com.openclassrooms.mddapi.infrastructure.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthPasswordValidator implements ConstraintValidator<AuthValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.isBlank()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        boolean isValid = true;

        if (password.length() < 8 || password.length() > 250) {
            addViolation(context, "Password must be between 8 and 250 characters");
            isValid = false;
        }

        if (!password.matches(".*[A-Z].*")) {
            addViolation(context, "Password must contain at least one uppercase letter");
            isValid = false;
        }

        if (!password.matches(".*[a-z].*")) {
            addViolation(context, "Password must contain at least one lowercase letter");
            isValid = false;
        }

        if (!password.matches(".*[0-9].*")) {
            addViolation(context, "Password must contain at least one digit");
            isValid = false;
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            addViolation(context, "Password must contain at least one special character");
            isValid = false;
        }

        return isValid;
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
    }
}
