package com.openclassrooms.mddapi.infrastructure.user.repository.port;

import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

public interface UserDataPort {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User getByEmail(String email) throws NotFoundException;
    User getById(Long id) throws NotFoundException;
    User getByUserName(String userName) throws NotFoundException;
    void save(User user);
}