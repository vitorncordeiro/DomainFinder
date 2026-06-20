package com.domainsugester.domain_finder.dto.external.Hostinger;

import java.util.Set;

public record HostingerTldAvaliabilityRequest(
    String domain,
    Set<String> tlds,
    Boolean withAlternatives
) {
}
