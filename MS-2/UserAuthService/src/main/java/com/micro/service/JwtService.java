package com.micro.service;

import com.micro.entity.UserData;
import com.micro.repo.UserRepo;
import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.security.Security;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    private UserRepo userRepo;

    public boolean validateToken(final String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);

        return claimsJws != null;
    }


    public String generateToken(Authentication authentication) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username",authentication.getName());

        claims.put("roles",authentication.getAuthorities());

        return createToken(claims, authentication.getName());
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setIssuer(SecurityConstant.JWT_ISSUER)
                .setSubject(SecurityConstant.JWT_SUB)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.JWT_VALID_TILL))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstant.JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}