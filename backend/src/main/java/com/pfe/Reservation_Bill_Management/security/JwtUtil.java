package com.pfe.Reservation_Bill_Management.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	
	final SecretKey key = Keys.hmacShaKeyFor("my-very-secure-and-long-secret-key-of-at-least-32-bytes!".getBytes());

    public String generateToken(String email, Long id) {
    	
    	
    	return Jwts.builder()
    		    .setSubject(email)
    		    .claim("id", id)
    		    .setIssuedAt(new Date())
    		    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
    		    .signWith(key)
    		    .compact();
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Long extractProfessorId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

}
