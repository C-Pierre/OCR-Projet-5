package com.openclassrooms.mddapi.application.comment.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String content,
        Long postId,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
