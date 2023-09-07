package com.cach.CachApp.service;

import com.cach.CachApp.model.ApiResponse;
import com.cach.CachApp.util.ApiConstants;
import com.cach.CachApp.util.IntEndpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;


@Service
@Slf4j
@CacheConfig(cacheNames = "productsCache") // Cache name
public class TestService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(cacheNames = "productsCache", key = "''", unless = "#result == null")
    public ApiResponse testMethod(String key){

        String stamp = LocalDateTime.now().toString();

        log.info("Inside testMethod()..!");

        return restCallGeneric(key, IntEndpoints.Technical_Signal);
    }

    @CacheEvict(cacheNames = "productsCache", allEntries = true)
    public void clearAllCache (){
        log.info("Inside clearAllCache()..!");
    }

    public ApiResponse restCallGeneric(String key, String intEndpoints){

        log.info("Inside restCallGeneric() .. !!");

        String url = ApiConstants.BASE_URL_INT+intEndpoints;

        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", key);
        headers.setContentType(MediaType.TEXT_PLAIN);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                Object.class
        );

        log.info("RestCall made to: "+intEndpoints+" endpoint.");

        ApiResponse body = new ApiResponse(response.getBody());

        return body;
    }

}
