package com.openclassrooms.mddapi.auth.jwt;

import java.util.Date;
import org.slf4j.Logger;
import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import com.openclassrooms.mddapi.auth.service.UserDetailsImpl;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.token.secret}")
    private String jwtSecret;

    @Value("${app.token.expiration}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(String.valueOf(userPrincipal.getId()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public Long getUserIdFromJwtToken(String token) {
        String subject = Jwts.parser()
            .setSigningKey(jwtSecret)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

        return Long.parseLong(subject);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}