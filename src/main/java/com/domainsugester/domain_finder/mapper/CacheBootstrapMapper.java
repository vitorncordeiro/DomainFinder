package com.domainsugester.domain_finder.mapper;

import com.domainsugester.domain_finder.dto.response.CacheBootstrapResponse;

import java.util.Map;

public class CacheBootstrapMapper {
    public static CacheBootstrapResponse toCacheResponse(Map<String, String> service, String descripton, String version){
        return new CacheBootstrapResponse(descripton, version, service);
    }
}
