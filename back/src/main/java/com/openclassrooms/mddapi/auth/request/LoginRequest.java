package com.openclassrooms.mddapi.auth.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username or email must not be blank")
    @Size(max = 250, message = "Identifier must be 250 characters max")
    String identifier,

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 250, message = "Password must be between 6 and 250 characters")
    String password
) {}
