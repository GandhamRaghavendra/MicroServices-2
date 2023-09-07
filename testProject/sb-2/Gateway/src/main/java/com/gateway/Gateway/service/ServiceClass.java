package com.gateway.Gateway.service;

import com.gateway.Gateway.model.MktData;
import com.gateway.Gateway.util.ApiConstants;
import com.gateway.Gateway.util.IntEndpoints;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceClass {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    private final Logger logger = LoggerFactory.getLogger(ServiceClass.class);

//    @Scheduled(fixedRate = 120000)
    public void restCallToTechnical_Signal(){

        logger.info("Inside Gateway Scheduled() method..!");

        logger.info("Inside callRestTemplate() .. !!");

        String url = ApiConstants.BASE_URL_INT+IntEndpoints.Technical_Signal;

        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", ApiConstants.API_KEY);
        headers.setContentType(MediaType.TEXT_PLAIN);

        ResponseEntity<MktData> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(null, headers),  // Pass the headers here
                MktData.class
        );

        logger.info("RestCall Happened ..!");

        Cache longCache = cacheManager.getCache("longCache");

        longCache.put(IntEndpoints.Technical_Signal, response.getBody());
    }
}
