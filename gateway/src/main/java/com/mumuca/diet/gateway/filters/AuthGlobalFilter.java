package com.mumuca.diet.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

import static com.mumuca.diet.gateway.filters.FilterOrder.AUTH_FILTER_ORDER;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/v1/calculator",
            "/v1/auth/sign-in",
            "/v1/auth/sign-up"
    );
    private static final String COOKIE_NAME = "jwt";

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isJwtValid(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    private String getJwtFromCookie(ServerWebExchange exchange) {
        return exchange.getRequest().getCookies().getFirst(COOKIE_NAME) != null
                ? "Bearer " + exchange.getRequest().getCookies().getFirst(COOKIE_NAME).getValue()
                : null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (!isJwtValid(authHeader)) {
            authHeader = getJwtFromCookie(exchange);
        }

        if (!isJwtValid(authHeader)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return AUTH_FILTER_ORDER;
    }
}