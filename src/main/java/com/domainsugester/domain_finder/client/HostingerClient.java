package com.domainsugester.domain_finder.client;

import com.domainsugester.domain_finder.config.FeignConfig;
import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvailabilityRequest;
import com.domainsugester.domain_finder.dto.external.Hostinger.HostingerTldAvaliabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name="hostingerClient", url="${HOSTINGER_BASE_URL}", configuration = FeignConfig.class)
public interface HostingerClient {
    @PostMapping(
            value="/api-proxy/api/available-tlds-by-theme",
    consumes = "application/json",
    produces = "application/json")

    Set<HostingerTldAvaliabilityResponse> getHostingerTldAvaliabilities(@RequestBody HostingerTldAvailabilityRequest request);
}
