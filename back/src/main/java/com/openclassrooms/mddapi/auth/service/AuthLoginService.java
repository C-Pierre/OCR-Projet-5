package com.openclassrooms.mddapi.auth.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.auth.jwt.JwtUtils;
import org.springframework.security.core.Authentication;
import com.openclassrooms.mddapi.auth.request.LoginRequest;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.auth.jwt.response.JwtResponse;
import com.openclassrooms.mddapi.common.exception.type.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthLoginService {

    private final AuthenticationManager authenticationManager;
    private final UserDataPort userDataPort;
    private final JwtUtils jwtUtils;

    public AuthLoginService(
            AuthenticationManager authenticationManager,
            UserDataPort userDataPort,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.userDataPort = userDataPort;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse execute(LoginRequest loginRequest) {
        String identifier = loginRequest.identifier();
        User user = null;

        if (identifier.contains("@")) {
            user = userDataPort.getByEmail(identifier);
        } else {
            user = userDataPort.getByUserName(identifier);
        }

        if (user == null) {
            throw new BadRequestException("Invalid credentials");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUserName(),
                loginRequest.password()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        );
    }
}
