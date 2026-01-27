package com.openclassrooms.mddapi.user.dto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String userName,
        String email,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}