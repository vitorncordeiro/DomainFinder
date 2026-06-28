package com.domainsugester.domain_finder.dto.external.Hostinger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record HostingerTldAvailabilityRequest(
    String domain,
    Set<String> tlds,
    @JsonProperty("with_alternatives")
    Boolean withAlternatives
) {
}
