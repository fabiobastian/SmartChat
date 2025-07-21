package com.cloudhumans.smartchat.dto;

import java.util.List;

public record EmbeddingData(
        String object,
        int index,
        List<Float> embedding
) {}
