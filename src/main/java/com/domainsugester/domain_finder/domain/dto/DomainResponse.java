package com.domainsugester.domain_finder.domain.dto;

import java.time.Instant;

public record DomainResponse(
        String domainName,
        Boolean avaliable,
        Instant created
) {
}
