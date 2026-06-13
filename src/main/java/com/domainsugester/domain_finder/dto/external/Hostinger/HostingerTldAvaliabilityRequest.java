package com.domainsugester.domain_finder.dto.external.Hostinger;

import java.util.List;

public record HostingerTldAvaliabilityRequest(
    String domain,
    List<String> tlds,
    Boolean withAlternatives
) {
}
