package com.domainsugester.domain_finder.service.hostinger;

import com.domainsugester.domain_finder.client.HostingerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostingerTldAvailabilityService {
    private final HostingerClient hostingerClient;

}
