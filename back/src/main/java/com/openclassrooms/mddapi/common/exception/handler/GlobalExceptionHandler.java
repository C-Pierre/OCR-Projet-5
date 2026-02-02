package com.openclassrooms.mddapi.common.exception.handler;

import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.openclassrooms.mddapi.common.error.response.ErrorResponse;
import com.openclassrooms.mddapi.common.error.builder.ErrorResponseBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseBuilder builder;

    public GlobalExceptionHandler(ErrorResponseBuilder builder) {
        this.builder = builder;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest request) {
        HttpStatus status = resolveHttpStatus(ex);
        String message;

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            StringBuilder sb = new StringBuilder();
            for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
                if (!sb.isEmpty()) sb.append("; ");
                sb.append(error.getDefaultMessage());
            }
            message = sb.toString();
            status = HttpStatus.BAD_REQUEST;
        } else {
            message = ex.getMessage();
        }

        return ResponseEntity
            .status(status)
            .body(builder.build(status, message, request));
    }


    private HttpStatus resolveHttpStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
