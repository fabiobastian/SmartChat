package com.cloudhumans.smartchat.dto.embedding;

import com.cloudhumans.smartchat.dto.chat.Usage;

import java.util.List;

public record EmbeddingResponse(
        String object,
        List<EmbeddingData> data,
        String model,
        Usage usage
) {}
