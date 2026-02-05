package com.openclassrooms.mddapi.api.user.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import com.openclassrooms.mddapi.infrastructure.auth.validator.AuthValidPassword;

public record UpdateUserRequest(
        @Size(max = 250, message = "Username must be 250 characters max")
        String userName,

        @Size(max = 250, message = "Email must be 250 characters max")
        @Email(message = "Email format is invalid")
        String email,

        @AuthValidPassword
        String password
) {}
