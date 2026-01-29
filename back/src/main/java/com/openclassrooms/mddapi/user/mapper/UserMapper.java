package com.openclassrooms.mddapi.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.openclassrooms.mddapi.user.entity.User;
import com.openclassrooms.mddapi.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDto dto);
}
