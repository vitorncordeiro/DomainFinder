package com.domainsugester.domain_finder.config;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.EncodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper; // ← Jackson 3, não fasterxml

@Configuration
public class FeignConfig {

    @Value("${hostinger.api.token}")
    private String token;

    @Bean
    public RequestInterceptor hostingerAuthInterceptor() {
        return template -> template.header("Authorization", "Bearer " + token);
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return (object, bodyType, template) -> {
            try {
                template.body(objectMapper.writeValueAsBytes(object), java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new EncodeException(e.getMessage(), e);
            }
        };
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return (response, type) -> {
            if (response.body() == null) return null;
            return objectMapper.readValue(
                    response.body().asInputStream(),
                    objectMapper.constructType(type)
            );
        };
    }
}