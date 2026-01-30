package com.openclassrooms.mddapi.post.request;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must be 200 characters max")
        String title,

        @NotBlank(message = "Content must not be blank")
        @Size(max = 5000, message = "Content must be 5000 characters max")
        String content,

        @NotNull(message = "Subject ID must not be null")
        Long subjectId,

        @NotNull(message = "Author ID must not be null")
        Long authorId
) {}
