package com.domainsugester.domain_finder.service;

import com.domainsugester.domain_finder.client.RdapClient;

import com.domainsugester.domain_finder.dto.request.DomainRequest;
import com.domainsugester.domain_finder.dto.response.DomainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainService {
    private RdapClient feignClient;

    public DomainResponse getDomain(DomainRequest request){

        return null;
    }
}
