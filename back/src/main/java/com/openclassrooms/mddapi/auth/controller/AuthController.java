package com.openclassrooms.mddapi.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.mddapi.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.common.response.MessageResponse;
import com.openclassrooms.mddapi.auth.service.AuthRegisterService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthRegisterService authRegisterService;

    public AuthController(
        AuthRegisterService authRegisterService
    ) {
        this.authRegisterService = authRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authRegisterService.execute(registerRequest));
    }
}
