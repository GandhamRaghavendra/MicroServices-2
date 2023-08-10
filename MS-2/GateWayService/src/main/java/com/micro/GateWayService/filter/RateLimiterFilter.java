package com.micro.GateWayService.filter;

import com.micro.GateWayService.util.JwtUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterFilter extends AbstractGatewayFilterFactory<RateLimiterFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    public RateLimiterFilter(){super(Config.class);}

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//
//                    logger.error("Missing authorization header");

                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);

                    Jws<Claims> claimsJws = jwtUtil.validateToken(authHeader);

                    // getting sub name.
                    String name = (String) claimsJws.getBody().get("username");

                    System.out.println(claimsJws.getBody().get("roles"));

                    // if bucket is not present.
                    if(!userBuckets.containsKey(name)){

                        // define the limit 1 time per 10 minute
                        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));

                        // construct the bucket
                        Bucket bucket = Bucket.builder().addLimit(limit).build();

                        userBuckets.put(name,bucket);
                    }

                    // if bucket is present.
                    Bucket bucket = userBuckets.get(name);

                    //
                    if(!bucket.tryConsume(1)) throw new RuntimeException("Limit exceeded wait for 1 minute");

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }


}
