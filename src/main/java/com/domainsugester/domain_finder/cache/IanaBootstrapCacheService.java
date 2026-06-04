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

@Service
@RequiredArgsConstructor
public class IanaBootstrapCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> ops = redisTemplate.opsForValue();


    public void save(String key, String value, Duration ttl){
        ops.set(key, value, ttl);
    }

    public Optional<String> fetchTldServer(String tld){
        String tldServer = ops.get(tld).toString();
        return Optional.ofNullable(tldServer);
    }
    public CacheBootstrapResponse fetchBootstrap(){
        Set<String> keys = scanKeys("iana:tld:*");
        Map<String, String> map = new HashMap<>();
        keys.forEach(key -> {
            map.put(key, ops.get(key).toString());
        });
        Optional<String> description = Optional.ofNullable(ops.get("iana:description").toString());
        Optional<String> version = Optional.ofNullable(ops.get("iana:version").toString());

        return CacheBootstrapMapper.toCacheResponse(map, description.toString(), version.toString());
    }
    private Set<String> scanKeys(String keyPrefix){
        Set<String> keys = new HashSet<>();

        ScanOptions options = ScanOptions.scanOptions()
                .match(keyPrefix)
                .count(100)
                .build();
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.keyCommands().scan(options))) {

            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
        }
        return keys;
    }
}