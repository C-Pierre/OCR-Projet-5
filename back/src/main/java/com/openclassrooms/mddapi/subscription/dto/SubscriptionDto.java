package com.openclassrooms.mddapi.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionDto(
        Long id,
        Long subjectId,
        Long userId,
        LocalDateTime createdAt
) {}
