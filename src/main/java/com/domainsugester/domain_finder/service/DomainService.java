package com.domainsugester.domain_finder.service;

import com.domainsugester.domain_finder.service.cache.HostingerTldCacheService;
import com.domainsugester.domain_finder.client.RdapClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final RdapClient rdapClient;
    private final HostingerTldCacheService hostingerTldCacheService;

    public String getDomain(String domain){
        System.out.println(domain);
        String rdapUrl = getRdapUrl(domain);
        URI uri = URI.create(rdapUrl);

        try {
            Object domainInfo = rdapClient.getDomainInfo(uri, domain);
            System.out.println(domainInfo);
            return domainInfo.toString();
        }catch (FeignException e){
            return "Available domain";
        }
    }
    private String getRdapUrl(String domain) {
        String[] parts = domain.split("\\.");
        String tld;
        if(parts.length > 2) {
            tld = parts[parts.length - 2] + "." + parts[parts.length - 1];
        }else{
            tld = parts[parts.length - 1];
        }
        return hostingerTldCacheService.fetchRdapUrl(tld);
    }
}
