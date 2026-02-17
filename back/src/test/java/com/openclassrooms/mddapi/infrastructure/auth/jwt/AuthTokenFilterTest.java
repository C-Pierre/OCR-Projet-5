package com.openclassrooms.mddapi.infrastructure.auth.jwt;

import com.openclassrooms.mddapi.infrastructure.auth.jwt.AuthTokenFilter;
import com.openclassrooms.mddapi.infrastructure.auth.jwt.JwtUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import jakarta.servlet.FilterChain;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsImpl;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidHeaderFormat_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Invalid token");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidJwt_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.jwt");
        when(jwtUtils.validateJwtToken("invalid.jwt")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils).validateJwtToken("invalid.jwt");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ValidJwt_ShouldAuthenticateUser() throws Exception {
        String token = "valid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserIdFromJwtToken(token)).thenReturn(1L);

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(1L);
        userDetails.setUsername("testUser");
        userDetails.setEmail("test@mail.com");
        userDetails.setPassword("password");

        when(userDetailsService.loadUserById(1L)).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication()
            instanceof UsernamePasswordAuthenticationToken);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ExceptionThrown_ShouldNotBreakChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenThrow(new RuntimeException("boom"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
