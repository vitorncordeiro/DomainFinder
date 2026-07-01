package com.domainsugester.domain_finder.service;

import com.domainsugester.domain_finder.cache.TldBootstrapCacheService;
import com.domainsugester.domain_finder.service.hostinger.HostingerTldAvailabilityService;
import com.domainsugester.domain_finder.service.iana.IanaBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TldBootStrapService {
    private final HostingerTldAvailabilityService availabilityService;
    private final IanaBootstrapService ianaBootstrapService;
    private final TldBootstrapCacheService tldBootstrapCacheService;

    public void refreshBootstrap(){
        Map<String, String> parsedIanaBootStrap = ianaBootstrapService.getIanaParsedBootstrap();
        parsedIanaBootStrap.forEach((tld, rdapUrl) -> {
            tldBootstrapCacheService.save(tld, rdapUrl);
        });
        System.out.println(availabilityService.getAvailableTlds());
    }
    private

}
