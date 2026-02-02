package com.openclassrooms.mddapi.common.exception.handler;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.openclassrooms.mddapi.common.error.response.ErrorResponse;
import com.openclassrooms.mddapi.common.error.builder.ErrorResponseBuilder;

class GlobalExceptionHandlerTest {

    private ErrorResponseBuilder builder;
    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        builder = mock(ErrorResponseBuilder.class);
        handler = new GlobalExceptionHandler(builder);
        request = mock(HttpServletRequest.class);
    }

    @Test
    void handleAny_shouldReturnInternalServerError_forGenericException() {
        Exception ex = new Exception("Something went wrong");

        ErrorResponse expectedResponse = mock(ErrorResponse.class);
        when(builder.build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", request))
            .thenReturn(expectedResponse);

        ErrorResponse response = handler.handleAny(ex, request).getBody();

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isEqualTo(response);
    }

    @Test
    void handleAny_shouldReturnBadRequest_forMethodArgumentNotValidException() {
        FieldError fieldError = new FieldError("user", "email", "Email invalide");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
        when(validationEx.getBindingResult()).thenReturn(bindingResult);

        ErrorResponse expectedResponse = mock(ErrorResponse.class);
        when(builder.build(HttpStatus.BAD_REQUEST, "Email invalide", request))
            .thenReturn(expectedResponse);

        ErrorResponse response = handler.handleAny(validationEx, request).getBody();

        var responseEntity = handler.handleAny(validationEx, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(response);
    }

    @Test
    void handleAny_shouldUseResponseStatusAnnotation_ifPresent() {
        @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
        class NotFoundException extends RuntimeException {
            NotFoundException(String message) { super(message); }
        }

        NotFoundException ex = new NotFoundException("Not found");
        ErrorResponse expectedResponse = mock(ErrorResponse.class);

        when(builder.build(HttpStatus.NOT_FOUND, "Not found", request))
            .thenReturn(expectedResponse);

        ErrorResponse response = handler.handleAny(ex, request).getBody();

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo(response);
    }
}
