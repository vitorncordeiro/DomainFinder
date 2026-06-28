package com.domainsugester.domain_finder.controller;
import com.domainsugester.domain_finder.dto.response.CacheBootstrapResponse;
import com.domainsugester.domain_finder.service.iana.IanaBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/ianaBootstrap")
@RequiredArgsConstructor
public class IanaBootstrapController {
    private final IanaBootstrapService bootstrapService;

    @GetMapping
    public ResponseEntity<CacheBootstrapResponse> getCachedBootstrap(){
        var response = bootstrapService.getCachedBootstrap();
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshIanaBootStrap(){
        bootstrapService.getIanaParsedBootstrap();
        return ResponseEntity.accepted().build();
    }
}
