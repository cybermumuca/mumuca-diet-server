package com.mumuca.diet.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Incoming Request: {} {} from {}", request.getMethod(), request.getRequestURI(), request.getHeader("X-Client-IP"));

        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);

        long duration = System.currentTimeMillis() - startTime;

        log.info("Outgoing Response: {} {} [{}] ({}ms)",
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.valueOf(response.getStatus()),
                duration
        );
    }
}