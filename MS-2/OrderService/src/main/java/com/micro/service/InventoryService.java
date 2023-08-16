package com.micro.service;

import com.micro.model.InventoryItem;
import com.micro.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CacheConfig(cacheNames = "inventoryCache") // Cache name
public class InventoryService {

    @Autowired
    private RestTemplate restTemplate;


    @Cacheable(cacheNames = "inventoryCache", key = "''", unless = "#result == null")
    public List<InventoryItem> getAllInventoryData() {

        String url = "http://localhost:8081/inventory";

        // Make the REST call
        ResponseEntity<List<InventoryItem>> inventoryData = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<InventoryItem>>() {});

        return inventoryData.getBody();
    }


}
