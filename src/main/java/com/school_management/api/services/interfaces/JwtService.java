package com.school_management.api.services.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    <T> T extractClaim(String token, Function<Claims, T> claimsTResolver);
    boolean isTokenValid(String token, UserDetails userDetails);
    String encryptPassword(String password);
    String extractEmail(String token);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
