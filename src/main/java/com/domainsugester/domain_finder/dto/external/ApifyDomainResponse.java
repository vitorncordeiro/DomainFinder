package com.domainsugester.domain_finder.dto.external;

import java.time.Instant;
import java.util.List;

public record ApifyDomainResponse(
        String domain,
        Boolean avaliable,
        Boolean registered,
        String method,
        String registrar,
        Instant createDate,
        Instant expiryDate,
        List<String> nameServers,
        Integer checkTimeMs,
        Boolean error,
        Instant checkedAt
) {
}
