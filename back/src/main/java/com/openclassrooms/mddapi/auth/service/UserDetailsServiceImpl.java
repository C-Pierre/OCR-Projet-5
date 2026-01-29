package com.openclassrooms.mddapi.auth.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.mddapi.user.repository.port.UserDataPort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDataPort userDataPort;

    public UserDetailsServiceImpl(UserDataPort userDataPort) {
        this.userDataPort = userDataPort;
    }

    public UserDetails loadUserById(Long id) {

        User user = userDataPort.getById(id);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "Utilisateur non trouvé avec le id : " + id
            );
        }

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUserName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getPassword());

        return userDetails;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userDataPort.getByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "Utilisateur non trouvé avec le username : " + username
            );
        }

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUserName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getPassword());

        return userDetails;
    }
}