package com.openclassrooms.mddapi.api.auth.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import com.openclassrooms.mddapi.infrastructure.auth.validator.AuthValidPassword;

public record LoginRequest(
    @NotBlank(message = "Username or email must not be blank")
    @Size(max = 250, message = "Identifier must be 250 characters max")
    String identifier,

    @NotBlank(message = "Password must not be blank")
    @AuthValidPassword
    String password
) {}
