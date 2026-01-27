package com.openclassrooms.mddapi.user.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        @Size(max = 250, message = "Username must be 250 characters max")
        String userName,

        @Size(max = 250, message = "Email must be 250 characters max")
        @Email(message = "Email format is invalid")
        String email,

        @Size(min = 6, max = 250, message = "Password must be between 6 and 250 characters")
        String password
) {}
