package com.openclassrooms.mddapi.user.repository.port;

import com.openclassrooms.mddapi.user.model.User;
import com.openclassrooms.mddapi.common.exception.NotFoundException;

public interface UserDataPort {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User getByEmail(String email) throws NotFoundException;
    User getById(Long id) throws NotFoundException;
    User getByUserName(String userName) throws NotFoundException;
    void save(User user);
}