package com.domainsugester.domain_finder.scheduler;

import com.domainsugester.domain_finder.service.iana.IanaBootstrapService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TldBootStrapScheduler {
    private final IanaBootstrapService service;

    @PostConstruct
    public void init() {
        refresh();
    }

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000L)
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    public void refresh(){
        service.refreshIanaBootStrap();
    }

}
