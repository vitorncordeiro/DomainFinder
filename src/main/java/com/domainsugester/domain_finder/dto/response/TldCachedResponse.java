package com.domainsugester.domain_finder.dto.response;

import java.util.Map;

public interface TldCachedResponse <V> {
    Map<String, V> tlds();
}
