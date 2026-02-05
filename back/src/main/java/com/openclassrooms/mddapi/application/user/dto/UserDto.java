package com.openclassrooms.mddapi.application.user.dto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String userName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}