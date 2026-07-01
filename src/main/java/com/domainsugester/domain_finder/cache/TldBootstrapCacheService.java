package com.domainsugester.domain_finder.cache;

import com.domainsugester.domain_finder.dto.response.CacheBootstrapResponse;
import com.domainsugester.domain_finder.mapper.CacheBootstrapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

public interface TldBootstrapCacheService{

    void save(String key, String value);
    CacheBootstrapResponse fetchBootstrap();
}