package com.domainsugester.domain_finder.service.hostinger;

import com.domainsugester.domain_finder.dto.external.iana.IanaBootstrapResponse;
import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class HostingerTldAvailabilityService {

    @Value("${playwright.server.url:}")
    private String playwrightServerUrl;

    public Map<String, String> getAvailableTlds() {
        try (Playwright playwright = Playwright.create(
                new Playwright.CreateOptions()
                        .setEnv(Map.of("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1"))
        )) {
            Browser browser = playwright.chromium().connect(
                    playwrightServerUrl,
                    new BrowserType.ConnectOptions().setTimeout(30_000)
            );

            Page page = browser.newPage();
            Response response = page.waitForResponse(
                    r -> r.url().contains("available-tlds-by-theme"),
                    () -> page.navigate(
                            "https://www.hostinger.com/domain-name-results" +
                                    "?from=homepage&domain=dawda.com"
                    )
            );

            String result = response.text();
            log.info("TLDs Successfully catched. Status: {}", response.status());

            browser.close();
            return parseAvaliableTlds(result);

        } catch (Exception e) {
            throw new RuntimeException("Error while capturing TLDs", e);
        }
    }
    private Map<String, String> parseAvaliableTlds(String tlds){
        Map<String, String> map = new HashMap<>();
        tlds = tlds.substring(tlds.indexOf("[") + 1, tlds.lastIndexOf("]"));
        String[] tldArray = tlds.split(",");
        for (String tld : tldArray) {
            tld = tld.substring(1, tld.length() - 1);
            map.put("hostinger:tld:" + tld, tld);
        }
        return map;
    }
}