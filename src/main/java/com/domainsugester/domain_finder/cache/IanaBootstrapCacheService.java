package com.domainsugester.domain_finder.cache;

import com.domainsugester.domain_finder.dto.external.iana.IanaBootstrapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IanaBootstrapCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, String value, Duration ttl){
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, ttl);
    }
}