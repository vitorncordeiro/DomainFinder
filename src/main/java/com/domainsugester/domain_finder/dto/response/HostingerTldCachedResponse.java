package com.domainsugester.domain_finder.dto.response;

import java.util.Map;

public record HostingerTldCachedResponse(
        Map<String, Boolean> tlds
) implements TldCachedResponse{
}
