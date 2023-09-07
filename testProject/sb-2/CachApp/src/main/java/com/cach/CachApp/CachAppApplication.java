package com.cach.CachApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CachAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachAppApplication.class, args);
	}

}
