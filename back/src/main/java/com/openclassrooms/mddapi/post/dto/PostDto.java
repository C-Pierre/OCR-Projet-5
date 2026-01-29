package com.openclassrooms.mddapi.post.dto;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String content,
        Long subjectId,
        String subjectName,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
