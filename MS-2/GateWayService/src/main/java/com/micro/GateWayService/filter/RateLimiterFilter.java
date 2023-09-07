package com.micro.GateWayService.filter;

import com.micro.GateWayService.util.JwtUtil;
import com.micro.GateWayService.util.RateLimit;
import com.micro.GateWayService.util.RateLimitUtil;
import com.micro.GateWayService.util.UuidService;
import com.netflix.discovery.converters.Auto;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    @Autowired
    private UuidService uuidService;

    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    public RateLimiterFilter(){super(Config.class);}

    @Override
    public GatewayFilter apply(Config config) {

        logger.info("Inside RateLimitFilter of (GATEWAY)..");

        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

//                System.out.println("Path: "+exchange.getRequest().getPath());

                logger.info("Path: "+exchange.getRequest().getPath());

//                System.out.println(validator.isSecured.test(exchange.getRequest()));

                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

                    logger.error("Missing authorization header");

                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader  , String.class);

                    Jws<Claims> claimsJws = jwtUtil.validateToken(authHeader);

                    // getting name from claims.
                    String name = (String) claimsJws.getBody().get("username");

                    // getting RequestPlan from claims.
                    String requestLimitPlan = (String) claimsJws.getBody().get("limit");

                    RateLimit rateLimit = RateLimit.valueOf(requestLimitPlan);

                    //getting key of the user from claims.
                    String key = (String) claimsJws.getBody().get("key");


                    // setting requestId and userKey to headers.
                    exchange.getRequest().mutate()
                                    .header("RequestId", uuidService.generateAndReturnUUID())
                                    .header("key", key);

                    String requestId = exchange.getRequest().getHeaders().getFirst("RequestId");
                    String userKey = exchange.getRequest().getHeaders().getFirst("key");

                    logger.info("RequestId set to headers: {}", requestId);
                    logger.info("User_key set to headers: {}", userKey);

                    // if bucket is not present.
                    if(!userBuckets.containsKey(name)){

                        // this method will take limit and  give related bucket.
                        Bucket bucket = RateLimitUtil.createBucketForRateLimit(rateLimit);

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
