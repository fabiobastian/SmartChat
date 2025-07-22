package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ChatCompletionRequest(
        String model,
        Double temperature,
        Integer max_tokens,
        List<MessageDTO> messages
) {}
