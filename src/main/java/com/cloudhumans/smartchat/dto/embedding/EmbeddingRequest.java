package com.cloudhumans.smartchat.dto.embedding;

public record EmbeddingRequest(
        String model,
        String input
) {
}
