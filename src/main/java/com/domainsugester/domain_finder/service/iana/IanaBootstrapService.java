package com.domainsugester.domain_finder.service.iana;

import com.domainsugester.domain_finder.cache.TldBootstrapCacheService;
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
    private final TldBootstrapCacheService ianaBootstrapCacheService;
    private final IanaBootstrapClient ianaBootstrapClient;
    private final Duration TTL = Duration.ofDays(10);

    public Map<String, String> getIanaParsedBootstrap(){

        IanaBootstrapResponse ianaResponse = ianaBootstrapClient.getIanaBootstrap();
        Map<String, String> parsedBootStrap = ianaBootstrapParser.parseRawBootstrap(ianaResponse);

        return parsedBootStrap;
    }
    public CacheBootstrapResponse getCachedBootstrap(){
        return ianaBootstrapCacheService.fetchBootstrap();
    }
}
