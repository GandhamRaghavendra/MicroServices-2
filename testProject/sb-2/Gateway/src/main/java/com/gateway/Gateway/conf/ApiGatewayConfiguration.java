package com.gateway.Gateway.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class ApiGatewayConfiguration {


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {

        CaffeineCache sortCache = new CaffeineCache("sortCache",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(10, TimeUnit.SECONDS)
                        .build()
        );

        CaffeineCache longCache = new CaffeineCache("longCache",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .build()
        );


        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.asList(sortCache, longCache);

        cacheManager.setCaches(caches);

        return cacheManager;
    }

//    @Bean
//    public GatewayFilter getBasicGatewayFilter(){
//        return new CacheFilter();
//    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))

                .route("advisory", r -> r.path("/api/advisory/**")
                        .uri("lb://advisory-service"))

                .route("marketplace", r -> r.path("/api/marketplace/**")
                        .uri("lb://marketplace-service"))

                .route("ent", r -> r
                        .path("/fno/**")
                        .filters(f -> f
                                .setRequestHeader("api-key", "futZ90Ky-jm"))
                        .uri("http://localhost:8080/fno"))
                .build();
    }
}
