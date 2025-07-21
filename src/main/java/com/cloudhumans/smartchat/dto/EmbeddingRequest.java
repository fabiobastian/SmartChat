package com.cloudhumans.smartchat.dto;

public record EmbeddingRequest(
        String model,
        String input
) {
}
