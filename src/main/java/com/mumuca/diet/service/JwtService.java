package com.mumuca.diet.service;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtService {
    Jwt decode(String token);
}
