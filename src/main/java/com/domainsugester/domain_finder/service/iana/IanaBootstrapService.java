package com.domainsugester.domain_finder.service.iana;

import com.domainsugester.domain_finder.cache.IanaBootstrapCacheService;
import com.domainsugester.domain_finder.client.IanaBootstrapClient;
import com.domainsugester.domain_finder.dto.external.iana.IanaBootstrapResponse;
import com.domainsugester.domain_finder.dto.response.CacheBootstrapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IanaBootstrapService {
    private final IanaBootstrapParser ianaBootstrapParser;
    private final IanaBootstrapCacheService ianaBootstrapCacheService;
    private final IanaBootstrapClient ianaBootstrapClient;
    private final Duration TTL = Duration.ofDays(10);

    public Map<String, String> refreshIanaBootStrap(){

        IanaBootstrapResponse ianaResponse = ianaBootstrapClient.getIanaBootstrap();
        Map<String, String> parsedBootStrap = ianaBootstrapParser.parseRawBootstrap(ianaResponse);

        parsedBootStrap.forEach((key, value) -> {
            ianaBootstrapCacheService.save(key, value, TTL);
        });

        return parsedBootStrap;
    }
    public CacheBootstrapResponse getCachedBootstrap(){
        return ianaBootstrapCacheService.fetchBootstrap();
    }
}
