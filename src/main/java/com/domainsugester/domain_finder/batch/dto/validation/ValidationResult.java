package com.domainsugester.domain_finder.batch.dto.validation;

import java.util.List;

public record ValidationResult(
        List<String> validDomains, List<LineError> errors
) {}
