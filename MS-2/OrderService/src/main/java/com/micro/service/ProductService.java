package com.micro.service;

import com.micro.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@CacheConfig(cacheNames = "productsCache") // Cache name
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;


    @Cacheable(cacheNames = "productsCache", key = "''", unless = "#result == null")
    public List<Product> getAllProducts() {

        String url = "http://localhost:8082/products";

        // Make the REST call
        ResponseEntity<List<Product>> products = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});

        return products.getBody();
    }
}
