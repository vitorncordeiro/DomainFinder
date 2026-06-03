package com.domainsugester.domain_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
@EnableFeignClients
public class DomainFinder {

	public static void main(String[] args) {
		SpringApplication.run(DomainFinder.class, args);
	}

}

