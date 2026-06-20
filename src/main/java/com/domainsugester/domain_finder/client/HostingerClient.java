package com.domainsugester.domain_finder.client;

import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvaliabilityRequest;
import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvaliabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@FeignClient(name="hostingerClient", url="${HOSTINGER_BASE_URL}")
public interface HostingerClient {
    @PostMapping("/api/domains/v1/availability")
    Set<HostingerTldAvaliabilityResponse> getHostingerTldAvaliabilities(HostingerTldAvaliabilityRequest request);
}
