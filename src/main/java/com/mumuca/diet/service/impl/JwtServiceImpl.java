package com.mumuca.diet.service.impl;

import com.mumuca.diet.service.JwtService;
import com.mumuca.diet.util.KeyUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final Environment env;

    // TODO: gambiarra
    @Override
    public Jwt decode(String token) {
        String publicKeyStr = env.getProperty("jwt.public.key");
        var publicKey = KeyUtils.parsePublicKey(publicKeyStr);
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        return jwtDecoder.decode(token);
    }
}
