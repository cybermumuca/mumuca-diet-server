package com.mumuca.diet.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Value("${rate-limiter.public-route.replenish-rate}")
    private int publicRouteReplenishRate;

    @Value("${rate-limiter.public-route.burst-capacity}")
    private int publicRouteBurstCapacity;

    @Value("${rate-limiter.public-route.requested-token}")
    private int publicRouteRequestedToken;

    @Value("${rate-limiter.private-route.replenish-rate}")
    private int privateRouteReplenishRate;

    @Value("${rate-limiter.private-route.burst-capacity}")
    private int privateRouteBurstCapacity;

    @Value("${rate-limiter.private-route.requested-token}")
    private int privateRouteRequestedToken;

    @Bean(name = "publicRouteRateLimiter")
    @Primary
    public RedisRateLimiter publicRouteRateLimiter() {
        return new RedisRateLimiter(publicRouteReplenishRate, publicRouteBurstCapacity, publicRouteRequestedToken);
    }

    @Bean(name = "privateRouteRateLimiter")
    public RedisRateLimiter privateRouteRateLimiter() {
        return new RedisRateLimiter(privateRouteReplenishRate, privateRouteBurstCapacity, privateRouteRequestedToken);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                     + "-" +
                     exchange.getRequest().getHeaders().getFirst("User-Agent")
        );
    }
}
