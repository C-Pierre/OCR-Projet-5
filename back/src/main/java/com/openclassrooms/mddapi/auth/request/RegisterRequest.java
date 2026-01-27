package com.openclassrooms.mddapi.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username must not be blank")
    @Size(max = 250, message = "Username must be 250 characters max")
    String userName,

    @NotBlank(message = "Email must not be blank")
    @Size(max = 250, message = "Email must be 250 characters max")
    @Email(message = "Email format is invalid")
    String email,

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 250, message = "Password must be between 6 and 250 characters")
    String password
) {}
