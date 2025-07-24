package com.cloudhumans.smartchat.config;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai.chat")
@Getter @Setter
public class OpenAiChatProperties {
    @NotBlank
    private String model;
    @Min(0) @Max(2)
    private double temperature;
    @Positive
    private int maxTokens;
}
