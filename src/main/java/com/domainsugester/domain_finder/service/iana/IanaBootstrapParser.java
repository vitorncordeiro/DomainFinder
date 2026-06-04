package com.domainsugester.domain_finder.service.iana;

import com.domainsugester.domain_finder.dto.external.iana.IanaBootstrapResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IanaBootstrapParser {
    public Map<String, String> parseRawBootstrap(IanaBootstrapResponse bootstrap){
        Map<String, String> map = new HashMap<>();
        map.put("iana:description", bootstrap.description());
        map.put("iana:version", bootstrap.version());
        bootstrap.services().forEach(pair -> {
            String rdapUrl = pair.get(1).get(0);
            pair.get(0).forEach(tld -> map.put(("iana:tld:" + tld), rdapUrl));
        });
        return map;
    }
}
