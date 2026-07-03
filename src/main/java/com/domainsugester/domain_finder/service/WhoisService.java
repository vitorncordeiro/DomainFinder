package com.domainsugester.domain_finder.service;

import com.domainsugester.domain_finder.client.IanaBootstrapClient;
import com.domainsugester.domain_finder.client.IanaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class WhoisService {
    private final IanaClient ianaClient;

    public String getWhoisServerUrl(String tld){
        String html = ianaClient.getTldInfoHtml(tld);
        String whoisServerUrl = extractWhoisServerUrl(html);
        return whoisServerUrl;
    }
    private String extractWhoisServerUrl(String html){
        Pattern pattern = Pattern.compile("whois\\.[^\\s<]+");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String whoisServer = matcher.group();
            return whoisServer;
        }
        return "unavailable";
    }
}
