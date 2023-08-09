package com.micro.GateWayService.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Configuration
public class GatewayConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

}
