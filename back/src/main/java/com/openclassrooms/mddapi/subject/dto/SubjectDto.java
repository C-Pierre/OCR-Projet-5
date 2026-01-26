package com.openclassrooms.mddapi.subject.dto;

import java.time.LocalDateTime;

public record SubjectDto(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}