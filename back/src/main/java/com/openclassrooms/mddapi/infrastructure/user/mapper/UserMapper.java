package com.openclassrooms.mddapi.infrastructure.user.mapper;

import org.mapstruct.Mapper;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.application.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
