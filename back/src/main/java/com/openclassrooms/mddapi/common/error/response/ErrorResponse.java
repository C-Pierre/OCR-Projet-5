package com.openclassrooms.mddapi.common.error.response;

import lombok.Getter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
