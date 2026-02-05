package com.openclassrooms.mddapi.application.user.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.openclassrooms.mddapi.application.user.dto.UserDto;
import com.openclassrooms.mddapi.infrastructure.user.mapper.UserMapper;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;

@Service
public class GetUserService {

    private final UserMapper userMapper;
    private final UserDataPort userDataPort;

    public GetUserService(
        UserMapper userMapper,
        UserDataPort userDataPort
    ) {
        this.userMapper = userMapper;
        this.userDataPort = userDataPort;
    }

    @Cacheable(value = "userCache", key = "#id")
    public UserDto execute(Long id) {
        return userMapper.toDto(userDataPort.getById(id));
    }
}
