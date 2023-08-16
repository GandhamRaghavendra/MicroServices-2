package com.micro.config;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.catalina.LifecycleState;
import org.slf4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class myConfig {

	@Bean
	public RestTemplate getresttemplate() {
		return new RestTemplate();
	}


	@Bean
	public CacheManager cacheManager() {
		CaffeineCache productsCache = new CaffeineCache("productsCache",
				Caffeine.newBuilder()
						.maximumSize(100)
						.expireAfterWrite(10, TimeUnit.MINUTES)
						.build()
		);

		CaffeineCache inventoryCache = new CaffeineCache("inventoryCache",
				Caffeine.newBuilder()
						.maximumSize(100)
						.expireAfterWrite(10, TimeUnit.MINUTES)
						.build()
		);

		CaffeineCache productCache = new CaffeineCache("productCache",
				Caffeine.newBuilder()
						.maximumSize(100)
						.expireAfterWrite(2, TimeUnit.MINUTES)
						.build()
		);


		SimpleCacheManager cacheManager = new SimpleCacheManager();

		List<Cache> caches = Arrays.asList(productsCache, inventoryCache, productCache);

		cacheManager.setCaches(caches);

		return cacheManager;
	}
}