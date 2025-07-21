package com.cloudhumans.smartchat.dto;

import java.util.List;

public record EmbeddingResponse(
        String object,
        List<EmbeddingData> data,
        String model,
        Usage usage
) {}
