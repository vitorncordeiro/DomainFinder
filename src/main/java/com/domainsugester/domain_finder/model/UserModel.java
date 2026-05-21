package com.domainsugester.domain_finder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Id
    private String id;

    @Indexed(unique = true)
    private String googleId;   // the "sub" field from Google — never changes

    private String name;
    private String email;
    private String pictureUrl;

    private Instant createdAt;
    private Instant lastLoginAt;
}
