package com.domainsugester.domain_finder.service.hostinger;

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

    public String getAvailableTlds() {
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
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error while capturing TLDs", e);
        }
    }
}