package com.openclassrooms.mddapi.infrastructure.user.repository.adapter;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.domain.user.entity.User;
import com.openclassrooms.mddapi.domain.user.repository.UserRepository;
import com.openclassrooms.mddapi.infrastructure.user.repository.port.UserDataPort;
import com.openclassrooms.mddapi.infrastructure.common.exception.type.NotFoundException;

@Service
public class UserDataAdapter implements UserDataPort {

    private final UserRepository userRepository;

    public UserDataAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName)
            .orElseThrow(() -> new NotFoundException("User not found with username: " + userName));
    }

    @Override
    public void save(User user) { userRepository.save(user); }
}

