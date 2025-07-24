package com.cloudhumans.smartchat.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Usage(
        @JsonProperty("prompt_tokens")
        int promptTokens,

        @JsonProperty("completion_tokens")
        int completionTokens,

        @JsonProperty("total_tokens")
        int totalTokens
) {}
