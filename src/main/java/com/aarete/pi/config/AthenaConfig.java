package com.aarete.pi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.AthenaClientBuilder;

@Configuration
public class AthenaConfig {

	@Bean
	public AthenaClientBuilder athenaClient() {
		return AthenaClient.builder()
					.credentialsProvider(DefaultCredentialsProvider.create());
	}
}
