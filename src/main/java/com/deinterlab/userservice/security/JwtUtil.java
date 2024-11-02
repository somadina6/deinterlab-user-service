package com.deinterlab.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.*;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Date;

/**
 * Utility class for JWT token generation and validation
 */
@Component
public class JwtUtil {

    /** Algorithm for the JWT token */
    private final SignatureAlgorithm alg = Jwts.SIG.RS512; //or PS512, RS256

    /**
     * Get the secret key for the JWT token
     *
     * @return secret key
     */
    private KeyPair getSecretKey() {
        return alg.keyPair().build();
    }


    /**
     * Generate a JWT token for the given username
     *
     * @param username String
     * @return JWT token
     */
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .signWith(getSecretKey().getPrivate(), alg)
                .issuedAt(new Date())
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
