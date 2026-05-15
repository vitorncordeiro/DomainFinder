package com.domainsugester.domain_finder.dto.response;

import java.time.Instant;

public record DomainResponse(
        String domainName,
        Boolean avaliable,
        Instant created
) {
}
