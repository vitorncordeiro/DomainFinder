package com.domainsugester.domain_finder.dto.response;

import com.domainsugester.domain_finder.tld.dto.TldCachedResponse;

import java.util.Map;

public record HostingerTldCachedResponse(
        Map<String, String> tlds
) implements TldCachedResponse {
}
