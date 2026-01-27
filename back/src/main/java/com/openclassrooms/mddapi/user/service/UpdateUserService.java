package com.openclassrooms.mddapi.user.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.model.User;
import com.openclassrooms.mddapi.user.dto.UserDto;
import com.openclassrooms.mddapi.user.mapper.UserMapper;
import com.openclassrooms.mddapi.user.request.UpdateUserRequest;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UpdateUserService {

    private final UserMapper userMapper;
    private final UserDataPort userDataPort;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserService(
        PasswordEncoder passwordEncoder,
        UserMapper userMapper,
        UserDataPort userDataPort
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userDataPort = userDataPort;
    }

    public UserDto execute(Long id, UpdateUserRequest request) {
        User user = userDataPort.getById(id);
        boolean modified = false;

        if (request.userName() != null && !request.userName().equals(user.getUserName())) {
            user.setUserName(request.userName());
            modified = true;
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            user.setEmail(request.email());
            modified = true;
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
            modified = true;
        }

        if (modified) {
            userDataPort.save(user);
        }

        return userMapper.toDto(user);
    }
}
