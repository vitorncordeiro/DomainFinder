package com.domainsugester.domain_finder.cache;

import com.domainsugester.domain_finder.dto.response.CacheBootstrapResponse;

public interface TldCacheService {

    void save(String key, String value);
    CacheBootstrapResponse fetchBootstrap();
}