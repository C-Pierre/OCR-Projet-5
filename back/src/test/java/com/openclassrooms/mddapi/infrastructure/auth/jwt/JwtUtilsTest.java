package com.openclassrooms.mddapi.infrastructure.auth.jwt;

import java.util.Date;

import com.openclassrooms.mddapi.infrastructure.auth.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import io.jsonwebtoken.SignatureAlgorithm;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.mddapi.application.auth.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.token.secret}")
    private String SECRET;

    @Value("${app.token.expiration}")
    private int EXPIRATION_MS;

    @BeforeEach
    void setUp() throws Exception {

        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, SECRET);

        Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtils, EXPIRATION_MS);
    }

    @Test
    void generateJwtToken_ShouldGenerateValidToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(42L);
        userDetails.setUsername("testUser");
        userDetails.setEmail("test@mail.com");
        userDetails.setPassword("password");

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    @Test
    void getUserIdFromJwtToken_ShouldReturnUserId() {
        String token = Jwts.builder()
            .subject("42")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();

        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        assertEquals(42L, userId);
    }

    @Test
    void validateJwtToken_ValidToken_ShouldReturnTrue() {
        String token = Jwts.builder()
            .subject("1")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_InvalidSignature_ShouldReturnFalse() {
        String token = Jwts.builder()
            .subject("1")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(
                SignatureAlgorithm.HS512,
                "FYvfkEBYUt39SSosBnrhFcNivCLgvrq4EONxpvcIgR3F7Q7cdB0MryMiB1IoRiNusplHcE8jC4c5KMMTtuVm5Fc478845877a94d8d5e952f27ca51d3348ee779136b"
            )
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_ExpiredToken_ShouldReturnFalse() {
        String token = Jwts.builder()
            .subject("1")
            .issuedAt(new Date(System.currentTimeMillis() - 10000))
            .expiration(new Date(System.currentTimeMillis() - 5000))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_MalformedToken_ShouldReturnFalse() {
        boolean isValid = jwtUtils.validateJwtToken(
            "FYvfkEBYUt39SSosBnrhFcNivCLgvrq4EONxpvcIgR3F7Q7cdB0MryMiB1IoRiNusplHcE8jC4c5KMMTtuVm5Fc478845877a94d8d5e952f27ca51d3348ee779136b"
        );

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forExpiredJwtException() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(jwtUtils);

        String expiredToken = Jwts.builder()
            .subject("john@test.com")
            .issuedAt(new Date(System.currentTimeMillis() - 20_000))
            .expiration(new Date(System.currentTimeMillis() - 10_000))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();

        assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forUnsupportedJwtException() {
        String unsupportedToken = "eyJhbGciOiJub25lIn0." + "eyJzdWIiOiIxIn0.";
        boolean result = jwtUtils.validateJwtToken(unsupportedToken);
        assertThat(result).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forIllegalArgumentException() {
        assertThat(jwtUtils.validateJwtToken(" ")).isFalse();
    }
}
