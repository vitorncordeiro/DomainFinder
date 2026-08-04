package com.domainsugester.domain_finder.batch.service;

import com.domainsugester.domain_finder.batch.dto.validation.LineError;
import com.domainsugester.domain_finder.batch.dto.validation.ValidationResult;
import com.domainsugester.domain_finder.batch.publisher.BatchPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final BatchPublisher batchPublisher;
    private final int MAX_LINES = 100;
    private final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    public String processTextFile(MultipartFile file) throws IOException {

        validate(file);
        ValidationResult result = validateAndReadDomains(file);
        for (String domain : result.validDomains()) {
            publishMessage(domain);
            System.out.println(result.errors());
        }
        return "ok";
    }
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "^(?=.{1,253}$)(?!-)[A-Za-z0-9-]{1,63}(?<!-)(\\.[A-Za-z0-9-]{1,63}(?<!-))+$"
    );

    public ValidationResult validateAndReadDomains(MultipartFile file) throws IOException {
        List<String> validDomains = new ArrayList<>();
        List<LineError> errors = new ArrayList<>();
        Set<String> checked = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber > MAX_LINES) {
                    errors.add(new LineError(lineNumber, "Limit of " + MAX_LINES + " lines exceeded."));
                    break;
                }

                String domain = line.trim().toLowerCase();

                if (domain.isEmpty()) continue; // ignora linhas em branco, sem erro

                if (!DOMAIN_PATTERN.matcher(domain).matches()) {
                    errors.add(new LineError(lineNumber, "Invalid domain: '" + domain + "'"));
                    continue;
                }

                if (!checked.add(domain)) {
                    errors.add(new LineError(lineNumber, "Domain duplicated: '" + domain + "'"));
                    continue;
                }

                validDomains.add(domain);
            }
        }

        return new ValidationResult(validDomains, errors);
    }

    private void publishMessage(String domain){
        System.out.println(domain);
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getContentType() == null || !file.getContentType().equals("text/plain")) {
            throw new IllegalArgumentException("File must be a text file");
        }
        if(file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
    }

}
