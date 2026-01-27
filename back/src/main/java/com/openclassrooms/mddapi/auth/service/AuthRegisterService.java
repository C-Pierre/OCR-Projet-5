package com.openclassrooms.mddapi.auth.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.model.User;
import com.openclassrooms.mddapi.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.common.response.MessageResponse;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.common.exception.BadRequestException;

@Service
public class AuthRegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserDataPort userDataPort;

    public AuthRegisterService(
        PasswordEncoder passwordEncoder,
        UserDataPort userDataPort
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDataPort = userDataPort;
    }

    public MessageResponse execute(RegisterRequest registerRequest) {
        if (userDataPort.existsByEmail(registerRequest.email())) {
            throw new BadRequestException("Error: Email is already taken!");
        }

        if (userDataPort.existsByUserName(registerRequest.userName())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        User user = new User(
            registerRequest.email(),
            registerRequest.userName()
        );

        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        userDataPort.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
