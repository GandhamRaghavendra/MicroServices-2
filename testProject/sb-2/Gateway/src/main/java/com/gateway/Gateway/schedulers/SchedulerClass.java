package com.gateway.Gateway.schedulers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class SchedulerClass {

    @Autowired
    private CacheManager cacheManager;


    @Scheduled(fixedRate = 300000) // Every 5 min it will run
    public void cleanUpMethod(){
        log.info("CacheCleanUpMethod() executed..");
    }

    @Scheduled(cron = "0 */5 9-15 * * MON-FRI") // Every 5 min, 9.00 AM - 3.00 PM, Mon - Fri
    public void runScheduledTask() {
        log.info("Scheduled task executed at "+ LocalDateTime.now());
    }
}

