package com.ictk.issuance.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component  // Add this annotation
public class JwtUtil {

    // Secret key (store securely, e.g., in application.properties)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration time (e.g., 1 hour)
    private static final long EXPIRATION_TIME = 3600000;

    // Create JWT
    public String generateToken(String userId, String sessionId) {
//        String sessionId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(userId)                          // Payload: User ID
                .setIssuer("ICTK")                          // Payload: Issuer
                .setIssuedAt(new Date())                    // Payload: Issued Time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Payload: Expiry
                .claim("sessionId", sessionId)               // Custom claim: Session ID
                .signWith(key, SignatureAlgorithm.HS256)    // Signature: HMAC SHA256
                .compact();
    }

    // Validate JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract User ID from JWT
    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
