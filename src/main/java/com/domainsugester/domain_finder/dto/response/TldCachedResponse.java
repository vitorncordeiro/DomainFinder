package com.domainsugester.domain_finder.dto.response;

import lombok.Builder;

import java.util.Map;

public record CacheBootstrapResponse(
        String description,
        String version,
        Map<String, String> service
) {
}
