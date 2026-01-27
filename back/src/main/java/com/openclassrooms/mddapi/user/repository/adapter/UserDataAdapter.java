package com.openclassrooms.mddapi.user.repository.adapter;

import com.openclassrooms.mddapi.common.exception.NotFoundException;
import com.openclassrooms.mddapi.user.model.User;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.repository.UserRepository;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;

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
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName)
            .orElseThrow(() -> new NotFoundException("User not found with username: " + userName));
    }

    @Override
    public void save(User user) { userRepository.save(user); }
}

