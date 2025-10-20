package com.school_management.api.services.impls;

import com.school_management.api.services.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;
    private final Long EXP_TIME = 1000 * 60L * 30 ; // 1000ms * 1m * 30m = 30 minutes


    /**
     * @param extraClaims 
     * @param userDetails
     * @return
     */
//    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return this.generateToken(extraClaims, userDetails, EXP_TIME);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expTime) {
        return Jwts.builder()
            .claims(Objects.isNull(extraClaims) ? Map.of() : extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expTime))
            .signWith(this.getSigningKey())
            .compact();
    }


    /**
     * @param token 
     * @param userDetails
     * @return
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = this.extractEmail(token);
        return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * @param token 
     * @return
     */
    @Override
    public String extractEmail(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    /**
     * @param token 
     * @param claimsTResolver
     * @param <T>
     * @return
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsTResolver) {
        final Claims claims = this.extracAllClaims(token);
        return claimsTResolver.apply(claims);
    }

    /**
     * @param token 
     * @return
     */
    private Claims extracAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) this.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * @param password incoming password for login (non encrypted)
     * @return same password encrypted
     */
    @Override
    public String encryptPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
