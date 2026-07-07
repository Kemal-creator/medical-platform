package com.medical.auth_service.service;

import com.medical.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    public void addToBlacklist(String token) {
        long ttlMs = jwtUtil.getExpirationMillis(token) - System.currentTimeMillis();
        if (ttlMs > 0) {
            redisTemplate.opsForValue()
                    .set(BLACKLIST_PREFIX + token, "true", Duration.ofMillis(ttlMs));
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
