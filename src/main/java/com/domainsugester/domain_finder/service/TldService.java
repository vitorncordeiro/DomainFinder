package com.domainsugester.domain_finder.service;

import com.domainsugester.domain_finder.service.cache.HostingerTldCacheService;
import com.domainsugester.domain_finder.service.cache.IanaBootstrapCacheService;
import com.domainsugester.domain_finder.service.hostinger.HostingerTldAvailabilityService;
import com.domainsugester.domain_finder.service.iana.IanaBootstrapService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TldService {
    private final HostingerTldAvailabilityService hostingerTldAvailabilityService;
    private final IanaBootstrapService ianaBootstrapService;
    private final HostingerTldCacheService hostingerTldCacheService;
    private final IanaBootstrapCacheService ianaTldCacheService;;

    public void refreshBootstrap(){
        Map<String, String> parsedIanaBootStrap = ianaBootstrapService.getIanaParsedBootstrap();
        parsedIanaBootStrap.forEach((tld, rdapUrl) -> {
            ianaTldCacheService.save(tld, rdapUrl);
        });

        Map<String, String> hostingerTlds = hostingerTldAvailabilityService.getAvailableTlds();
        hostingerTlds.forEach((tld, rdapUrl) -> {
            hostingerTldCacheService.save(tld, rdapUrl);
        });
    }

    @PostConstruct
    public void teste(){
        hostingerTldCacheService.fetchBootstrap().tlds().forEach((tld, rdapUrl) -> {
            if (rdapUrl.equals("unavailable"))
                System.out.println(tld.split(":")[2]);
        });
    }


}
