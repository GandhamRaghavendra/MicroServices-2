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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.security.Security;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Autowired
    private UserRepo userRepo;

    public void validateToken(final String token) {

        if(token != null){

            try{

                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);

                String username = (String) claimsJws.getBody().get("username");

                List<String> roles = (List<String>) claimsJws.getBody().get("roles");

                List<GrantedAuthority> authorities = new ArrayList<>();

                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            catch (Exception exception){
                throw new BadCredentialsException("Invalid Token Received..!");
            }
        }
    }


    public String generateToken(Authentication authentication) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username",authentication.getName());

        claims.put("roles", getRole(authentication.getAuthorities()));

        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
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

    private List<String> getRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}