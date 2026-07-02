package com.domainsugester.domain_finder.mapper;

import com.domainsugester.domain_finder.dto.response.HostingerTldCachedResponse;
import com.domainsugester.domain_finder.dto.response.IanaTldCachedResponse;
import com.domainsugester.domain_finder.dto.response.TldCachedResponse;

import java.util.Map;

public class CacheBootstrapMapper {
    public static TldCachedResponse toIanaCacheResponse(Map<String, String> service, String descripton, String version){
        return new IanaTldCachedResponse(descripton, version, service);
    }
    public static TldCachedResponse toHosingerCacheResponse(Map<String, String> tlds){
        return new HostingerTldCachedResponse(tlds);
    }
}
