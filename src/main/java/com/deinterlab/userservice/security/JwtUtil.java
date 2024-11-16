package com.deinterlab.userservice.security;

import com.deinterlab.userservice.dto.UserDTO;
import com.deinterlab.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Date;
import java.util.UUID;

/**
 * Utility class for JWT token generation and validation
 */
@Component
public class JwtUtil {

    /** Algorithm for the JWT token */
    private final SignatureAlgorithm alg = Jwts.SIG.RS512; //or PS512, RS256


    /**
     * Constructor
     */
    public JwtUtil() {}

    /**
     * Get the secret key for the JWT token
     *
     * @return secret key
     */
    private KeyPair getSecretKey() {
        return alg.keyPair().build();
    }


    /**
     * Generate a JWT token
     *
     * @param user UserDTO
     * @return token
     */
    public String generateToken(UserDTO user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .signWith(getSecretKey().getPrivate(), alg)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .claim("userData", user)
                .compact();
    }

    /**
     * Validate the JWT token
     *
     * @param token String
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey().getPublic())
                    .build();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract the username from the JWT token
     *
     * @param token String
     * @return username
     */
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();  // Get the subject from the payload
    }

    /**
     * Extract all claims from the JWT token
     *
     * @param token String
     * @return claims
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey().getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }

    }

}
