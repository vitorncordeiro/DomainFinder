package com.domainsugester.domain_finder.controller;

import com.domainsugester.domain_finder.dto.request.DomainRequest;
import com.domainsugester.domain_finder.dto.response.DomainResponse;
import com.domainsugester.domain_finder.service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
public class DomainController {
    private DomainService domainService;

    @PostMapping("/{domain}")
    public ResponseEntity<DomainResponse> getDomain(@PathVariable DomainRequest domain){
        DomainResponse response = domainService.getDomain(domain);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
