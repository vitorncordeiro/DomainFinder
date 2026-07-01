package com.domainsugester.domain_finder.dto.response;

import com.domainsugester.domain_finder.cache.TldCacheService;

import java.util.Map;

public record IanaTldCachedBootstrap(
        String description,
        String version,
        Map<String, String> tlds
) implements TldCachedResponse {
}
