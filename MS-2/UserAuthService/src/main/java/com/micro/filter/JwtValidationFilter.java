package com.micro.filter;

import com.micro.service.JwtService;
import com.micro.service.SecurityConstant;
import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class JwtValidationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    private final Logger logger = LoggerFactory.getLogger(JwtValidationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("Inside JwtValidationFilter..");

        String jwt= request.getHeader(SecurityConstant.JWT_HEADER);

        //extracting the word Bearer
        jwt = jwt.substring(7);

        logger.info("JWT: "+jwt);


        if(jwt != null){

            try{

                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(jwt);

                String username = (String) claimsJws.getBody().get("username");

                logger.info("User_Name: "+username);

                List<String> roles = (List<String>) claimsJws.getBody().get("roles");

                if(roles.isEmpty()) logger.warn("User Don't have any roles..");

                List<GrantedAuthority> authorities = new ArrayList<>();

                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.info("Successfully Stored to Application Context..");
            }

            catch (Exception exception){
                throw new BadCredentialsException("Invalid Token Received..!");
            }
        }

        logger.info("Token validation Done..");

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return request.getServletPath().equals("/auths/token");
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstant.JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
