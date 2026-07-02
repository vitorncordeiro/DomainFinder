package com.domainsugester.domain_finder.client;

import com.domainsugester.domain_finder.dto.request.DomainRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Optional;

@FeignClient(name="RdapClient", url="${rdap.client.url}")
public interface RdapClient{
    @GetMapping("/domain/{domain}")
    Object getDomainInfo(URI uri, @PathVariable String domain);
}
