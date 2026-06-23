package com.example.subscription.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {

		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		log.info("Caffeine cache initialized");

		cacheManager.setCaffeine(
				Caffeine.newBuilder()
						.initialCapacity(10)
						.maximumSize(100)
						.expireAfterWrite(10, TimeUnit.MINUTES)
		);

		return cacheManager;
	}
}