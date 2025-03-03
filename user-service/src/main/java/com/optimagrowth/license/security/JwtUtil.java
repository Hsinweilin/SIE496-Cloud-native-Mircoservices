package com.optimagrowth.license.security;

import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String secretKey = "secret-key"; // Use a stronger key for production

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);  // Throws exception if invalid
            return true;
        } catch (JwtException e) {
            return false;  // Invalid token
        }
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
