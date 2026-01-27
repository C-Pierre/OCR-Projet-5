package com.openclassrooms.mddapi.user.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.dto.UserDto;
import com.openclassrooms.mddapi.user.mapper.UserMapper;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;

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

    public UserDto execute(Long id) {
        return userMapper.toDto(userDataPort.getById(id));
    }
}
