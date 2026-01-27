package com.openclassrooms.mddapi.security.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.user.model.User;
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
