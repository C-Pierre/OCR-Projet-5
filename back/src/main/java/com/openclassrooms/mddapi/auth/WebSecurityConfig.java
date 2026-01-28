package com.openclassrooms.mddapi.auth;

import org.springframework.http.HttpMethod;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import com.openclassrooms.mddapi.auth.jwt.JwtUtils;
import com.openclassrooms.mddapi.auth.jwt.AuthTokenFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.auth.service.UserDetailsServiceImpl;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                management -> management.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/subjects/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(
                    (request, response, exception) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
                    }
                )
            );

        return http.build();
    }
}
