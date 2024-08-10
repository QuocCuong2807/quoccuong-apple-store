package com.springteam.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();

        Date expireDate = new Date(new Date().getTime() + JwtConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setAudience(authentication.getAuthorities().toString())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JwtConstant.JWT_SECRET)
                .compact();
        return token;
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstant.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JwtConstant.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("jwt was expired or incorrect");
        }
    }
}
