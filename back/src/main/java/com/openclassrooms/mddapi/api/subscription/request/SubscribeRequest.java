package com.openclassrooms.mddapi.api.subscription.request;

import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull(message = "Subject ID must not be null")
        Long subjectId,

        @NotNull(message = "User ID must not be null")
        Long userId
) {}
