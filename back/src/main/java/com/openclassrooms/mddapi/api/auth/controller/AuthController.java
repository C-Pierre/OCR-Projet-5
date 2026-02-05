package com.openclassrooms.mddapi.api.auth.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.api.auth.request.LoginRequest;
import com.openclassrooms.mddapi.api.auth.response.JwtResponse;
import com.openclassrooms.mddapi.api.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.api.common.response.MessageResponse;
import com.openclassrooms.mddapi.application.auth.service.AuthLoginService;
import com.openclassrooms.mddapi.application.auth.service.AuthRegisterService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthLoginService authLoginService;
    private final AuthRegisterService authRegisterService;

    public AuthController(
        AuthLoginService authLoginService,
        AuthRegisterService authRegisterService
    ) {
        this.authLoginService = authLoginService;
        this.authRegisterService = authRegisterService;
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authLoginService.execute(loginRequest));
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Validation error or email/username already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authRegisterService.execute(registerRequest));
    }
}
