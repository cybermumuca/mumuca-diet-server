package com.mumuca.diet.security;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

import static com.mumuca.diet.config.CacheConfig.JWT_BLACKLIST_PREFIX;

@Component
@AllArgsConstructor
public class JwtBlacklist {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addToBlacklist(String token, Instant tokenExpiresAt) {
        long ttlInSeconds = Duration.between(Instant.now(), tokenExpiresAt).getSeconds();

        if (ttlInSeconds > 0) {
            redisTemplate
                    .opsForValue()
                    .set(
                            JWT_BLACKLIST_PREFIX.concat(token),
                            "blacklisted",
                            Duration.ofSeconds(ttlInSeconds)
                    );
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.opsForValue().get(JWT_BLACKLIST_PREFIX.concat(token)) != null;
    }
}
