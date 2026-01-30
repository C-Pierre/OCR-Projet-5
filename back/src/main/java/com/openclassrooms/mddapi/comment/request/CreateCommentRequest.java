package com.openclassrooms.mddapi.comment.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank(message = "Content must not be blank")
        @Size(max = 2500, message = "Content must be 2500 characters max")
        String content,

        @NotNull(message = "PostId must not be blank")
        Long postId,

        @NotNull(message = "UserId must not be blank")
        Long userId
) {}
