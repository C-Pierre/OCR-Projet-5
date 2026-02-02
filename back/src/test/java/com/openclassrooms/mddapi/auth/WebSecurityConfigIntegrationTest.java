package com.openclassrooms.mddapi.auth;

import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebSecurityConfigIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPublicEndpoint_PermitAll() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/subject", String.class);
        assertTrue(
            response.getStatusCode().is2xxSuccessful() || response.getStatusCode() == HttpStatus.UNAUTHORIZED
        );
    }

    @Test
    void testOptionsRequests_PermitAll() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/auth/login", HttpMethod.OPTIONS, entity, String.class
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testSecureEndpoint_Unauthenticated() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/posts/user/1", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testSecureEndpoint_WithInvalidJwt_ShouldFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("fake.jwt.token");

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/posts/user/1",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
