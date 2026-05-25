package com.domainsugester.domain_finder.dto.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String pictureUrl
    ) {}
