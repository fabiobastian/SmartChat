package com.cloudhumans.smartchat.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureSearchClientConfig {

    @Value("${azure.search.api.key}")
    private String apiKey;

    @Bean
    public RequestInterceptor azureSearchInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("api-key", apiKey);
        };
    }
}