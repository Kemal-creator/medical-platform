package com.medical.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    public void addToBlacklist(String token, long expirationMs) {
        long ttlMs = expirationMs - System.currentTimeMillis();
        if (ttlMs > 0) {
            redisTemplate.opsForValue()
                    .set(BLACKLIST_PREFIX + token, "true", Duration.ofMillis(ttlMs));
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
