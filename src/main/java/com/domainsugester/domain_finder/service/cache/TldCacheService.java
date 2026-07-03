package com.domainsugester.domain_finder.service.cache;

import com.domainsugester.domain_finder.dto.response.TldCachedResponse;

public interface TldCacheService {

    void save(String key, String value);
    TldCachedResponse fetchBootstrap();
}