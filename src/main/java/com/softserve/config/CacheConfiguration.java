package com.softserve.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableCaching
@PropertySource("classpath:cache.properties")
public class CacheConfiguration {

    private final Environment environment;

    @Autowired
    public CacheConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(environment.getProperty("redis.address", String.class));
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        Long ttl = Optional.ofNullable(environment.getProperty("ttl", Long.class)).orElse(60 * 60 * 1000L);
        Long maxIdleTime = Optional.ofNullable(environment.getProperty("maxIdleTime", Long.class)).orElse(30 * 60 * 1000L);

        config.put("map", new CacheConfig(ttl, maxIdleTime));
        config.put("semesterList", new CacheConfig(ttl, maxIdleTime));
        config.put("scheduleList", new CacheConfig(ttl, maxIdleTime));
        config.put("lessons", new CacheConfig(ttl, maxIdleTime));
        config.put("schedules", new CacheConfig(ttl, maxIdleTime));
        config.put("semesterSchedules", new CacheConfig(ttl, maxIdleTime));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
