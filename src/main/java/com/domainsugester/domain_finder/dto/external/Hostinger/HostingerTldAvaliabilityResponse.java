package com.domainsugester.domain_finder.dto.external.Hostinger;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HostingerTldAvaliabilityResponse(
        String domain,
        @JsonProperty("is_available")
        Boolean isAvailable,
        @JsonProperty("is_alternative")
        Boolean isAlternative,
        String restriction
) {
}
