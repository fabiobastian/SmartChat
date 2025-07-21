package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ChatCompletionRequest(
        String model,
        List<Message> messages,
        Integer max_tokens,
        Double temperature
) {}
