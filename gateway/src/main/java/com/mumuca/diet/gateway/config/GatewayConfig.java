package com.mumuca.diet.gateway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${apiUrl}")
    private String apiUrl;

    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            @Qualifier("privateRouteRateLimiter") RedisRateLimiter privateRouteRateLimiter,
            @Qualifier("publicRouteRateLimiter") RedisRateLimiter publicRouteRateLimiter,
            @Qualifier("ipKeyResolver") KeyResolver keyResolver
    ) {
        return builder
                .routes()
                .route(route -> route.path("/v1/**")
                        .filters(filter -> filter
                                .requestRateLimiter(config -> config.setRateLimiter(privateRouteRateLimiter).setKeyResolver(keyResolver))
                                .rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}")
                        )
                        .uri(apiUrl))
                .route(route -> route.path("/v1/calculator/**")
                        .filters(filter -> filter
                                .requestRateLimiter(c -> c.setRateLimiter(publicRouteRateLimiter).setKeyResolver(keyResolver))
                                .rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}"))
                        .uri(apiUrl))
                .build();
    }
}