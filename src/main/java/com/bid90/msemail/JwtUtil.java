package com.bid90.msemail;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final String secret;


    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    /**
     * Get subject (email) from token
     *
     * @param token
     * @return
     */
    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    /**
     * Get specific claim from token
     *
     * @param token
     * @param claim
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getClaim(String token, String claim, Class<T> tClass) {
        try {
            return JWT.decode(token).getClaim(claim).as(tClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token claim", e);
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return getClaim(token, "expirationAt", Date.class);
    }

    public boolean validateToken(String token) {
        var validation = JWT.require(Algorithm.HMAC512(secret)).build();
        try {
            validation.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }






}