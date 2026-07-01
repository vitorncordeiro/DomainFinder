package com.domainsugester.domain_finder.dto.response;

import java.util.Map;

public record IanaTldCachedResponse(
        String description,
        String version,
        Map<String, String> tlds
) implements TldCachedResponse {}
