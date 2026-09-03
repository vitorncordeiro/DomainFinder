package com.domainsugester.domain_finder.batch.cache;

import com.domainsugester.domain_finder.batch.dto.batch.BatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BatchCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void decr(UUID batchId){
        redisTemplate.opsForValue().decrement("batch:" + batchId.toString() + ":remaining");
    }
    public void saveRemaining(UUID batchId, Integer remaining){
        redisTemplate.opsForValue().set("batch:" + batchId.toString() + ":remaining", remaining);
    }
    public void saveBatchResult(UUID batchId, BatchResult batchResult){

        redisTemplate.opsForValue().set("batch:" + batchId.toString(), batchResult);

    }
    public Integer getBatchRemaining(UUID batchId){
        return (Integer) redisTemplate.opsForValue().get("batch:" + batchId.toString() + ":remaining");
    }
    public BatchResult getBatchResult(UUID batchId){
        return (BatchResult) redisTemplate.opsForValue().get("batch:" + batchId.toString());
    }
}
