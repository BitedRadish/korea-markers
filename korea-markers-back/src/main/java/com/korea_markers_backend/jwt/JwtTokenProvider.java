package com.korea_markers_backend.jwt;

import com.korea_markers_backend.user.entity.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long accessExpiration= Duration.ofMinutes(15).toMillis();
    private final long refreshExpiration=Duration.ofDays(14).toMillis();

    private SecretKey key;

    @PostConstruct
    public void init(){
        byte[] bytes=secretKey.getBytes(StandardCharsets.UTF_8);
        this.key= Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(CustomUserDetails userDetails){
        Date now=new Date();
        Date deadline=new Date(now.getTime()+accessExpiration);
        String jwtId= UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setId(jwtId)
                .setIssuedAt(now)
                .setExpiration(deadline)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpiration);
        String jwtId= UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setId(jwtId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getJwtId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
