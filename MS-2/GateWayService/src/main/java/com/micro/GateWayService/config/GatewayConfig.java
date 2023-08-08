package com.micro.GateWayService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("PRODUCTSERVICE", r -> r.path("/products/**")

                .uri("lb://PRODUCTSERVICE"))

            .route("INVENTORYSERVICE", r -> r.path("/inventory/**")

                .uri("lb://INVENTORYSERVICE"))

            .route("ORDERSERVICE", r -> r.path("/orders/**")

                .uri("lb://ORDERSERVICE"))

            .build();
    }
}
