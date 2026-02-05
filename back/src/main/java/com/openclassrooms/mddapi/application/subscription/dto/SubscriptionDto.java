package com.openclassrooms.mddapi.application.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionDto(
        Long id,
        Long subjectId,
        Long userId,
        LocalDateTime createdAt
) {}
