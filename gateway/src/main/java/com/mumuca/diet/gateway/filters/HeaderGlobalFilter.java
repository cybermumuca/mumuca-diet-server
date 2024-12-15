package com.mumuca.diet.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.mumuca.diet.gateway.filters.FilterOrder.HEADER_FILTER_ORDER;

@Component
public class HeaderGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-Forwarded-By", "Spring-Cloud-Gateway")
                .header("X-Request-Received-At", String.valueOf(System.currentTimeMillis()))
                .header("X-Trace-Id", UUID.randomUUID().toString())
                .header("X-Client-IP", exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
                .header("X-Gateway-Version", "1.0.0")
                .header("X-Gateway-Redirect", "true")
                .header("X-Max-Processing-Time", "5000ms")
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return HEADER_FILTER_ORDER;
    }
}
