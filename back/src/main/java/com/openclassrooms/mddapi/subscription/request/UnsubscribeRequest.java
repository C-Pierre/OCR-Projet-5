package com.openclassrooms.mddapi.subscription.request;

import jakarta.validation.constraints.NotNull;

public record UnsubscribeRequest(
        @NotNull(message = "Subject ID must not be null")
        Long subjectId,

        @NotNull(message = "User ID must not be null")
        Long userId
) {}
