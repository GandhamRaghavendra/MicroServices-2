package com.cach.CachApp.scheduler;

import com.cach.CachApp.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerClass {

    @CachePut(cacheNames = "productsCache", key = "''", unless = "#result == null")
    @Scheduled(fixedRate = 120000) // Every 2 minutes it will call.
    // @Scheduled(cron = "0 0/5 9-16 * * 1-5") // Every 5 minutes from 9 AM to 4 PM, Monday to Friday
    public ApiResponse updateProductsCache(){

        log.info("Inside updateproductsCache()..!");

        return null;
    }
}
