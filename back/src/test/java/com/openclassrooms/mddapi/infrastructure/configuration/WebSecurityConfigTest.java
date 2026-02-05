package com.openclassrooms.mddapi.infrastructure.configuration;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.infrastructure.auth.jwt.JwtUtils;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsServiceImpl;

class WebSecurityConfigTest {

    @Test
    void testBeansInitialization() throws Exception {
        UserDetailsServiceImpl userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
        JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);

        WebSecurityConfig config = new WebSecurityConfig(userDetailsService, jwtUtils);

        assertNotNull(config.authenticationProvider());
        assertNotNull(config.passwordEncoder());
        assertNotNull(config.authenticationJwtTokenFilter());
    }
}
