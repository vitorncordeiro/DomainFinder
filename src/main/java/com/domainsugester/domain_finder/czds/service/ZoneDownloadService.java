package com.domainsugester.domain_finder.czds.service;

import com.domainsugester.domain_finder.czds.client.AuthenticationClient;
import com.domainsugester.domain_finder.czds.client.ZoneFileDownloadClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZoneDownloadService {
    private final AuthenticationClient authenticationClient;
    private final ZoneFileDownloadClient zoneDownloadClient;
    @Value("${icann.account.username}")
    private String icannUsername;
    @Value("${icann.account.password}")
    private String icannPassword;
    private String bearerToken;

    public void downloadZoneFiles(){

    }

    private String authenticate(){
        if(bearerToken == null || bearerToken.isEmpty()) {
            bearerToken = authenticationClient.authenticate(icannUsername, icannPassword).accessToken();
        }
        return bearerToken;
    }
}
