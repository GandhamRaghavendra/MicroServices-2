package com.micro.service;

import com.micro.model.InventoryItem;
import com.micro.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@CacheConfig(cacheNames = "inventoryCache") // Cache name
public class InventoryService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheManager cacheManager;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);


    @Cacheable(cacheNames = "inventoryCache", key = "''", unless = "#result == null")
    public List<InventoryItem> getAllInventoryData() {

        logger.info("Inside getAllInventoryData() of OrderService..!");

        String url = "http://localhost:8081/inventory";

        // Make the REST call
        ResponseEntity<List<InventoryItem>> inventoryData = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<InventoryItem>>() {
                });

        return inventoryData.getBody();
    }


    //    @Cacheable(cacheNames = "inventoryCache", key = "''", unless = "#result == null")
    public void updateInventoryCache(List<InventoryItem> inventoryItems, Integer pId, Integer qun) {

        logger.info("Inside updateInventoryCache() of OrderService..!");

        logger.info("ProductId: " + pId + " Quantity: " + qun);

        Cache inventoryCache = cacheManager.getCache("inventoryCache");

        inventoryItems.stream()
                .filter(inv -> inv.getProductId() == pId)
                .findFirst()
                .ifPresent(inv -> {
                    logger.info("Quantity: " + inv.getQuantity());
                    int updatedQun = inv.getQuantity() - qun;

                    if (updatedQun >= 0) {
                        inv.setQuantity(updatedQun);
                        logger.info("Updated Data: " + inv);
                    } else {
                        throw new RuntimeException("Not enough quantity available for update.");
                    }
                });


        if (inventoryCache != null) {

            inventoryCache.put("inventoryItems", inventoryItems);

            logger.info("Updated the inventoryCache with new Data..!");

        } else throw new RuntimeException("Cache 'inventoryCache' not found.");
    }


}
