package com.domainsugester.domain_finder.client;

import com.domainsugester.domain_finder.dto.request.DomainRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="RdapClient")
public interface RdapClient {

}
