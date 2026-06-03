package com.domainsugester.domain_finder.scheduler;

import com.domainsugester.domain_finder.cache.IanaBootstrapCacheService;
import com.domainsugester.domain_finder.client.IanaBootstrapClient;
import com.domainsugester.domain_finder.dto.external.iana.IanaBootstrapResponse;
import com.domainsugester.domain_finder.service.iana.IanaBootstrapParser;
import com.domainsugester.domain_finder.service.iana.IanaBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IanaBootStrapScheduler {
    private final IanaBootstrapService service;

    @Scheduled
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    public void refresh(){
        service.refreshIanaBootStrap();
    }

}
