package com.tx.thetabackendapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
public class jwtUtil {
    //change in application.properties
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expires;
    
    //all of these are getters ordered by nesting naming scheme following existing spring boot methods as close as possible
    //claims
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    //tokens
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Instant currentTime = Instant.now();
        Instant expiration = currentTime.plus(expires, ChronoUnit.MILLIS);
        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername()).issuedAt(Date.from(currentTime)).expiration(Date.from(expiration)).signWith(getSigningKey()).compact();
    }
    
    public String buildToken(UserDetails userDetails) {
        return buildToken(Map.of(), userDetails);
    }
    
    public String buildToken(UserDetails userDetails, Integer userId, String role) {
        //user id and role are from users table in user.java
        Map<String, Object> claims = Map.of("userId", userId, "role", role);
        return buildToken(claims, userDetails);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Boolean oldToken(String token) {
        return getExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !oldToken(token));
    }
}