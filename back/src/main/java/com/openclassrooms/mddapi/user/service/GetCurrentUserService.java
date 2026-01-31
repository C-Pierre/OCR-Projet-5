package com.openclassrooms.mddapi.user.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.dto.UserDto;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.user.mapper.UserMapper;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.core.context.SecurityContextHolder;
import com.openclassrooms.mddapi.common.exception.UnauthorizedException;

@Service
public class GetCurrentUserService {

    private final UserDataPort userDataPort;
    private final UserMapper mapper;

    public GetCurrentUserService(
        UserDataPort userDataPort,
        UserMapper mapper
    ) {
        this.userDataPort = userDataPort;
        this.mapper = mapper;
    }

    public UserDto execute() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Vous n'êtes pas authentifié.");
        }

        User user = userDataPort.getByUserName(auth.getName());

        return mapper.toDto(user);
    }
}
