package com.openclassrooms.mddapi.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import com.openclassrooms.mddapi.auth.request.LoginRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.auth.service.AuthLoginService;
import com.openclassrooms.mddapi.common.response.MessageResponse;
import com.openclassrooms.mddapi.auth.service.AuthRegisterService;
import com.openclassrooms.mddapi.auth.jwt.response.JwtResponse;

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

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authLoginService.execute(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authRegisterService.execute(registerRequest));
    }
}
