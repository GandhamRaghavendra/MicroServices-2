package com.micro.config;
import org.slf4j.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class myConfig {

	@Bean
	public RestTemplate getresttemplate() {
		return new RestTemplate();
	}
}