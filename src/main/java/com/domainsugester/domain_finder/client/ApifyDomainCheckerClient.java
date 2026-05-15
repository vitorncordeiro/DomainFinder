package com.domainsugester.domain_finder.client;

import com.domainsugester.domain_finder.dto.external.ApifyDomainResponse;
import com.domainsugester.domain_finder.dto.request.DomainRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="ApifyClient", url="${APIFY_BASE_URL}")
public interface ApifyDomainCheckerClient{

    @PostMapping("${APIFY_RUN_URL}")
    List<ApifyDomainResponse> getDomain(@RequestBody DomainRequest domainRequest);
}
