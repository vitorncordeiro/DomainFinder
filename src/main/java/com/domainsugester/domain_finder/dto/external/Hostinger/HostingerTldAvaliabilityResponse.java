package com.domainsugester.domain_finder.dto.external.Hostinger;

public record HostingerTldAvaliabilityResponse(
        String domain,
        Boolean isAvaliable,
        Boolean isAlternative,
        String restriction
) {
}
